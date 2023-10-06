package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.LevelDescription
import de.haukesomm.sokoban.core.SokobanGame
import de.haukesomm.sokoban.core.SokobanGameFactory
import de.haukesomm.sokoban.web.components.*
import de.haukesomm.sokoban.web.components.game.MoveEvent
import de.haukesomm.sokoban.web.components.game.gameField
import de.haukesomm.sokoban.web.components.game.moveButtons
import de.haukesomm.sokoban.web.components.icons.*
import de.haukesomm.sokoban.web.theme.ThemePreference
import de.haukesomm.sokoban.web.theme.ThemePreferences
import dev.fritz2.core.*
import dev.fritz2.headless.components.disclosure
import kotlinx.browser.window
import kotlinx.coroutines.flow.*

class GameFrame {

    private val enabledGameConfigurations = storeOf(SokobanGameFactory.configurationOptions)

    private var gameFlow = MutableStateFlow(
        SokobanGameFactory.withMinimalConfiguration(
            additional = SokobanGameFactory.configurationOptions
        )
    )


    fun RenderContext.render() {
        gameFlow.render { game ->
            initializeLevelClearedAlert(game)

            div("h-full pb-8 w-full overflow-auto flex flex-col gap-4 justify-between items-center") {
                // Disable double-tap to zoom on mobile devices as this interferes with the
                // virtual gamepad.
                inlineStyle("touch-action: manipulation;")

                titleBar(game)

                div("max-w-min shrink-0 rounded-lg shadow overflow-hidden") {
                    gameField(game.state)
                }

                div {
                    moveButtons(game)
                }
            }
        }
    }

    private fun RenderContext.titleBar(game: SokobanGame) {
        val levelDescriptions = game.getAvailableLevels()
        val selectedLevelStore = storeOf(levelDescriptions.first())

        val initiallyEnabledRules = enabledGameConfigurations.current

        selectedLevelStore.data handledBy {
            game.loadLevel(it.id)
        }

        disclosure(
            """w-full py-2 px-4 flex flex-col items-stretch md:items-center bg-background-lightest dark:bg-background-dark
                | shadow-sm dark:shadow-md""".trimMargin()
        ) {
            div("w-full flex flex-row flex-wrap justify-between items-center gap-x-4 gap-y-6 text-sm") {
                div("w-full md:w-auto flex flex-row items-center gap-2"){
                    icon("w-7 h-7", definition = SokobanAppIcons.logo)
                    span("text-xl font-semibold text-primary-500 dark:text-primary-600") {
                        +"Sokoban"
                    }
                }
                div("grow md:max-w-xl flex flex-wrap gap-x-6 gap-y-2 items-center") {
                    div("grow w-full md:w-auto") {
                        listBox {
                            entries = levelDescriptions
                            format = LevelDescription::name
                            value(selectedLevelStore)
                        }
                    }
                    span("whitespace-nowrap") {
                        +"Moves: "
                        code {
                            game.state.map { it.moves }.render(into = this) {
                                +it.toString()
                            }
                        }
                    }
                    span("whitespace-nowrap") {
                        +"Pushes: "
                        code {
                            game.state.map { it.pushes }.render(into = this) {
                                +it.toString()
                            }
                        }
                    }
                    plainButton {
                        // TODO: Import new tailwind icons and use arrow-uturn-left
                        iconDefinition(HeroIcons.reply)
                        iconSize = PlainButton.IconSize.Small
                        text("Undo")
                    }.run {
                        title("Undo last move")
                        disabled(game.previousStateExists.map { !it })
                        clicks handledBy {
                            game.undoLastMoveIfPossible()
                        }
                    }
                    plainButton {
                        iconDefinition(HeroIcons.refresh)
                        iconSize = PlainButton.IconSize.Small
                        text("Reset")
                    }.run {
                        title("Reset level")
                        clicks handledBy {
                            game.reloadLevel()
                        }
                    }
                }
                disclosureButton(
                    """p-1 w-full md:w-auto flex flex-row gap-2 items-center rounded-sm focus:outline-none
                        | focus-visible:ring-2 focus-visible:ring-primary-500 
                        | focus-visible:dark:ring-primary-600""".trimMargin()
                ) {
                    opened.map {
                        if (it) HeroIcons.chevron_up
                        else HeroIcons.chevron_down
                    }.render {
                        icon("w-4 h-4", definition = it)
                    }
                    span { +"More options" }
                }
            }
            div("relative") {
                disclosurePanel {
                    div(
                        """absolute inset-x-0 flex justify-center bg-background-lightest
                            | dark:bg-background-dark shadow-sm dark:shadow-md""".trimMargin()
                    ) {
                        div(
                            classes(
                                """grow w-full md:w-auto max-w-none md:max-w-4xl m-4 p-4 rounded-md
                                    | grid grid-cols-1 md:grid-cols-3 gap-6
                                    | bg-background-light dark:bg-background-darkest""".trimMargin(),
                            )
                        ) {
                            withTitle("Configuration options") {
                                checkboxGroup {
                                    values(enabledGameConfigurations)
                                    options = SokobanGameFactory.configurationOptions
                                    optionsFormat = SokobanGameFactory.ConfigurationOption::name
                                    optionDescriptionFormat = SokobanGameFactory.ConfigurationOption::description
                                }
                                div {
                                    plainButton {
                                        text("Apply options and reload game")
                                        iconDefinition(HeroIcons.arrow_right)
                                        disabled(enabledGameConfigurations.data.map {
                                            it.toSet() == initiallyEnabledRules.toSet()
                                        })
                                    }.run {
                                        clicks.flatMapLatest { enabledGameConfigurations.data } handledBy {
                                            gameFlow.tryEmit(
                                                SokobanGameFactory.withMinimalConfiguration(
                                                    additional = it
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            withTitle("Theme") {
                                val preference = storeOf(ThemePreferences.getCurrentDarkModePreference())
                                preference.data.drop(1) handledBy ThemePreferences::setDarkModePreference

                                radioGroup<ThemePreference> {
                                    value(preference)
                                    options = ThemePreference.values().toList()
                                    optionsFormat = {
                                        when (it) {
                                            ThemePreference.AlwaysLight -> "Light"
                                            ThemePreference.AlwaysDark -> "Dark"
                                            ThemePreference.FollowSystem -> "System"
                                        }
                                    }
                                    optionDescriptionFormat = {
                                        when (it) {
                                            ThemePreference.FollowSystem -> "Follow the system's setting"
                                            else -> null
                                        }
                                    }
                                }
                            }
                            withTitle("About") {
                                iconLink(
                                    GitHubIcons.octocat,
                                    text = "Sokoban",
                                    description = VersionInfo.sokobanVersion,
                                    href = "https://github.com/haukesomm/sokoban"
                                )
                                iconLink(
                                    Fritz2Icons.fritz2,
                                    text = "Built with fritz2",
                                    description = VersionInfo.fritz2Version,
                                    href = "https://fritz2.dev"
                                )
                                p(
                                    """pt-2 flex flex-row gap-x-1 items-center text-xs border-t
                                        | border-dotted border-neutral-dark-secondary dark:border-neutral-light-secondary
                                        | """.trimMargin()
                                ) {
                                    +"Made with ♡ in Hamburg, Germany"
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun RenderContext.initializeLevelClearedAlert(game: SokobanGame) {
        game.state
            .filter { it.levelCleared }
            .map { } handledBy levelClearedAlert(game)
    }

    private fun RenderContext.levelClearedAlert(game: SokobanGame): Handler<Unit> =
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
                            game.state.render(into = this) {
                                it.run { +"You needed $moves moves and $pushes pushes." }
                            }
                        }
                    }
                }
            }
        }


    private fun RenderContext.moveButtons(game: SokobanGame) {
        moveButtons {
            val isTouchDevice =
                window.asDynamic().ontouchstart != undefined || window.navigator.maxTouchPoints > 0

            // Hide move buttons when not on mobile but still initialize button handling regardless:
            className(
                if (isTouchDevice) "block"
                else "hidden"
            )

            merge(
                moveEvents,
                Window.keydowns.mapNotNull {
                    when(shortcutOf(it.key)) {
                        Keys.ArrowUp -> MoveEvent.Up
                        Keys.ArrowDown -> MoveEvent.Down
                        Keys.ArrowLeft -> MoveEvent.Left
                        Keys.ArrowRight -> MoveEvent.Right
                        else -> null
                    }
                },
            ) handledBy { moveEvent ->
                game.run {
                    when(moveEvent) {
                        MoveEvent.Up -> movePlayerIfPossible(Direction.Top)
                        MoveEvent.Down -> movePlayerIfPossible(Direction.Bottom)
                        MoveEvent.Left -> movePlayerIfPossible(Direction.Left)
                        MoveEvent.Right -> movePlayerIfPossible(Direction.Right)
                    }
                }
            }
        }
    }
}

fun RenderContext.gameFrame() =
    GameFrame().run { render() }