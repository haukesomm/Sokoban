package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.web.components.icons.GitHubIcons
import de.haukesomm.sokoban.web.components.icons.icon
import de.haukesomm.sokoban.web.level.BundledLevelRepository
import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.GameStateService
import de.haukesomm.sokoban.core.level.LevelDescription
import de.haukesomm.sokoban.web.components.*
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.level.BundledLevelTileFactory
import dev.fritz2.core.*
import kotlinx.coroutines.flow.*

fun main() {
    render {
        val darkModeStore = initDarkMode()

        val gameStateService = GameStateService(BundledLevelRepository(), BundledLevelTileFactory())
        val levelDescriptions = gameStateService.getAvailableLevels()
        val selectedLevelDescription = storeOf(levelDescriptions.first())

        val gameStateStore = storeOf<GameState?>(null)
        gameStateService.addGameStateChangedListener { state ->
            gameStateStore.update(state)
        }

        selectedLevelDescription.data handledBy { levelDescription ->
            gameStateService.loadLevel(levelDescription.id)
        }

        Window.keydowns.map { shortcutOf(it.key) } handledBy { shortcut ->
            if (gameStateStore.current?.levelCleared == false) {
                gameStateService.getPlayer()?.let { player ->
                    when(shortcut) {
                        Keys.ArrowUp -> gameStateService.moveEntityIfPossible(player, Direction.TOP)
                        Keys.ArrowDown -> gameStateService.moveEntityIfPossible(player, Direction.BOTTOM)
                        Keys.ArrowLeft -> gameStateService.moveEntityIfPossible(player, Direction.LEFT)
                        Keys.ArrowRight -> gameStateService.moveEntityIfPossible(player, Direction.RIGHT)
                    }
                }
            }
        }

        val showLevelClearedAlert = levelClearedAlert(gameStateStore.data.filterNotNull())
        gameStateStore.data
            .filterNotNull()
            .filter { it.levelCleared }
            .map { } handledBy showLevelClearedAlert

        div(
            """sticky w-full py-2 px-4 flex flex-row justify-between items-center gap-4
                | bg-white dark:bg-darkgray-400 shadow-sm dark:shadow-md
                | text-primary-800 dark:text-primary-200""".trimMargin()
        ) {
            span("text-xl font-semibold text-primary-500") {
                +"Sokoban"
            }
            div("flex gap-4 items-center grow max-w-sm") {
                div("grow") {
                    listBox {
                        entries = levelDescriptions
                        format = LevelDescription::name
                        value(selectedLevelDescription)
                    }
                }
                span("text-sm") {
                    +"Moves: "
                    code {
                        gameStateStore.data.filterNotNull().render(into = this) {
                            +it.moves.toString()
                        }
                    }
                }
                span("text-sm") {
                    +"Pushes: "
                    code {
                        gameStateStore.data.filterNotNull().render(into = this) {
                            +it.pushes.toString()
                        }
                    }
                }
                button {
                    type("button")
                    icon("w-4 h-4", definition = HeroIcons.refresh)
                    title("Reset level")
                }.clicks handledBy {
                    gameStateService.reload()
                }
            }
            div("flex flex-row items-center gap-4") {
                switch {
                    data(darkModeStore)
                    label = "Dark mode"
                }

                a {
                    icon(
                        "w-8 h-8 text-gray-800 text-gray-700 dark:text-gray-200",
                        definition = GitHubIcons.octocat
                    )
                    href("https://github.com/haukesomm/sokoban")
                    target("_blank")
                }
            }
        }

        main {
            div("w-full p-4 flex flex-col items-center") {
                div("max-w-min flex justify-center rounded-lg shadow overflow-hidden") {
                    gameField(gameStateStore.data.filterNotNull())
                }
            }
        }
    }
}

fun RenderContext.levelClearedAlert(states: Flow<GameState>): Handler<Unit> =
    alertModal {
        content {
            div("space-y-4") {
                p("font-semibold text-md") {
                    +"Level cleared!"
                }
                p {
                    +"Congratulations, you successfully finished this level!"
                    br { }
                    span {
                        states.render(into = this) {
                            it.run { +"You needed $moves moves and $pushes pushes." }
                        }
                    }
                }
            }
        }
    }