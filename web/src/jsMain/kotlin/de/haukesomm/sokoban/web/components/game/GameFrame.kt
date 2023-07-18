package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.GameStateService
import de.haukesomm.sokoban.core.level.LevelDescription
import de.haukesomm.sokoban.core.moving.rules.MoveRule
import de.haukesomm.sokoban.core.moving.rules.MultipleBoxesPreventingMoveRule
import de.haukesomm.sokoban.core.moving.rules.WallCollisionPreventingMoveRule
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.web.DarkModeStore
import de.haukesomm.sokoban.web.VersionInfo
import de.haukesomm.sokoban.web.components.alertModal
import de.haukesomm.sokoban.web.components.checkboxGroup
import de.haukesomm.sokoban.web.components.icons.GitHubIcons
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import de.haukesomm.sokoban.web.components.listBox
import de.haukesomm.sokoban.web.components.switch
import de.haukesomm.sokoban.web.level.BundledLevelRepository
import de.haukesomm.sokoban.web.level.BundledLevelTileFactory
import dev.fritz2.core.*
import kotlinx.coroutines.flow.*

class GameFrame {

    companion object {
        private val availableRules = listOf(
            MultipleBoxesPreventingMoveRule(),
            WallCollisionPreventingMoveRule()
        )
    }

    private val gameStateService = GameStateService(
        BundledLevelRepository(),
        BundledLevelTileFactory()
    ).apply {
        setCustomRules(availableRules.toSet())
    }

    private val levelDescriptions = gameStateService.getAvailableLevels()


    private fun RenderContext.initializeKeyboardInput() {
        Window.keydowns.map { shortcutOf(it.key) } handledBy { shortcut ->
            gameStateService.getPlayer()?.let { player ->
                when(shortcut) {
                    Keys.ArrowUp -> gameStateService.moveEntityIfPossible(player, Direction.Top)
                    Keys.ArrowDown -> gameStateService.moveEntityIfPossible(player, Direction.Bottom)
                    Keys.ArrowLeft -> gameStateService.moveEntityIfPossible(player, Direction.Left)
                    Keys.ArrowRight -> gameStateService.moveEntityIfPossible(player, Direction.Right)
                }
            }
        }
    }


    fun RenderContext.render() {
        val nullableGameStateStore = storeOf<GameState?>(null)
        val gameStateFlow = nullableGameStateStore.data.filterNotNull()
        gameStateService.addGameStateChangedListener { state ->
            nullableGameStateStore.update(state)
        }

        val selectedLevelStore = storeOf(levelDescriptions.first())
        selectedLevelStore.data handledBy {
            gameStateService.loadLevel(it.id)
        }

        val enabledRulesStore = storeOf(availableRules)
        enabledRulesStore.data handledBy {
            gameStateService.setCustomRules(it)
        }


        initializeKeyboardInput()
        gameStateFlow
            .filter { it.levelCleared }
            .map { } handledBy levelClearedAlert(gameStateFlow)


        div("h-full w-full flex flex-col gap-4") {
            titleBar(gameStateFlow, selectedLevelStore)

            main("mx-4 grow grid grid-cols-[fit-content(400px)_auto] gap-4") {
                sideBar(enabledRulesStore)
                gameFieldContainer(gameStateFlow)
            }

            footerBar()
        }
    }

    private fun RenderContext.titleBar(
        gameStateFlow: Flow<GameState>,
        selectedLevelStore: Store<LevelDescription>
    ) {
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
                        value(selectedLevelStore)
                    }
                }
                span("text-sm") {
                    +"Moves: "
                    code {
                        gameStateFlow.render(into = this) {
                            +it.moves.toString()
                        }
                    }
                }
                span("text-sm") {
                    +"Pushes: "
                    code {
                        gameStateFlow.render(into = this) {
                            +it.pushes.toString()
                        }
                    }
                }
                button {
                    type("button")
                    icon("w-4 h-4", definition = HeroIcons.refresh)
                    title("Reset level")
                }.clicks handledBy {
                    gameStateService.reloadLevel()
                }
            }
            div("flex flex-row items-center gap-4") {
                switch {
                    data(DarkModeStore)
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
    }

    private fun RenderContext.sideBar(
        enabledRulesStore: Store<List<MoveRule>>
    ) {
        div("p-4 space-y-4 max-w-md bg-white dark:bg-darkgray-400 rounded-md shadow") {
            div("flex gap-2 items-center") {
                span("font-semibold text_white dark:text-gray-300") {
                    +"Edit Rules"
                }
                div(
                    """px-1.5 py-0.5 flex items-center gap-1 rounded-full border border-primary-500 
                                | text-xs text-primary-500""".trimMargin()
                ) {
                    icon("w-3 h-3", definition = HeroIcons.beaker)
                    +"Experimental"
                }
            }
            checkboxGroup {
                values(enabledRulesStore)
                options = availableRules
                optionsFormat = MoveRule::title
                optionDescriptionFormat = MoveRule::description
            }
        }
    }

    private fun RenderContext.footerBar() {
        div("w-full p-4 flex flex-row justify-center gap-4 text-xs text-gray-400 font-light") {
            span { +"Sokoban" }
            span { +"Version: ${VersionInfo.version}" }
            span {
                +"Commit Hash: "
                a {
                    +VersionInfo.commitHash
                    target("_blank")
                    href("https://github.com/haukesomm/Sokoban/tree/${VersionInfo.commitHash}")
                }
            }
        }
    }

    private fun RenderContext.gameFieldContainer(
        gameStateFlow: Flow<GameState>
    ) {
        div("w-full flex flex-col items-center") {
            div("max-w-min flex justify-center rounded-lg shadow overflow-hidden") {
                gameField(gameStateFlow)
            }
        }
    }

    private fun RenderContext.levelClearedAlert(gameStateFlow: Flow<GameState>): Handler<Unit> =
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
                            gameStateFlow.render(into = this) {
                                it.run { +"You needed $moves moves and $pushes pushes." }
                            }
                        }
                    }
                }
            }
        }
}

fun RenderContext.gameFrame() =
    GameFrame().run { render() }