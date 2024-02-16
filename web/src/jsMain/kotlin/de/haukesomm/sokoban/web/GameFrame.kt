package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.web.components.*
import de.haukesomm.sokoban.web.components.game.MoveEvent
import de.haukesomm.sokoban.web.components.game.gameField
import de.haukesomm.sokoban.web.components.game.moveButtons
import de.haukesomm.sokoban.web.components.icons.*
import de.haukesomm.sokoban.web.theme.ThemePreference
import de.haukesomm.sokoban.web.theme.ThemePreferences
import dev.fritz2.core.*
import kotlinx.browser.window
import kotlinx.coroutines.flow.*

class GameFrame(context: RenderContext) : RenderContext by context {

    private val availableMoveRuleOptions = MoveRuleOption.values().asList()
    private val enabledGameConfigurations = storeOf(availableMoveRuleOptions)


    private var gameFlow = MutableStateFlow(
        SokobanGameFactory.withMinimalConfiguration(
            additionalRules = availableMoveRuleOptions.map(MoveRuleOption::moveRule)
        )
    )


    fun render() {
        gameFlow.render { game ->
            levelClearedAlert(game)

            div("pb-4 w-full h-full overflow-auto flex flex-col gap-4 justify-between items-center") {
                // Disable double-tap to zoom on mobile devices as this interferes with the
                // virtual gamepad.
                inlineStyle("touch-action: manipulation;")

                titleBar(game)

                div("max-w-min shrink-0 rounded-lg shadow overflow-hidden") {
                    gameField(game.state)
                }

                moveButtons(game)

                footer()
            }
        }
    }

    private fun RenderContext.titleBar(game: SokobanGame) {
        val levelDescriptions = game.getAvailableLevels()
        val selectedLevelStore = storeOf(levelDescriptions.first())

        selectedLevelStore.data handledBy {
            game.loadLevel(it.id)
        }
        game.levelDescription handledBy selectedLevelStore.update


        div("w-full") {
            div(
                """py-2 px-4 grid grid-cols-1 lg:grid-cols-3 items-center gap-4 text-sm
                    | bg-background-lightest dark:bg-background-dark shadow-sm dark:shadow-md
                """.trimMargin()
            ) {
                div("w-full lg:w-auto flex flex-row items-center gap-2"){
                    icon("w-7 h-7", definition = CustomIcons.sokoban)
                    span("text-xl font-semibold text-primary-500 dark:text-primary-600") {
                        +"Sokoban"
                    }
                }
                div(
                    """w-full lg:max-w-sm lg:justify-self-center grid grid-cols-1 lg:grid-cols-2 gap-4 
                        | items-center""".trimMargin()
                ) {
                    div("grow") {
                        listBox {
                            entries = levelDescriptions
                            format = LevelDescription::name
                            value(selectedLevelStore)
                        }
                    }
                    div("flex lg:justify-end items-center gap-4") {
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
                    }
                }
                div("w-full -mx-1 flex flex-row lg:justify-end gap-4") {
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
                    plainButton {
                        text("Settings")
                        iconDefinition(HeroIcons.cog)
                    }.run {
                        settingsModal(clicks.map {}) handledBy {}
                    }
                }
            }
        }
    }

    private fun settingsModal(trigger: Flow<Unit>): Flow<ModalResult<Unit>> {
        val initiallyEnabledRules = enabledGameConfigurations.current
        return modal(trigger) {
            title("Settings")
            content {
                div(
                    """w-full md:w-auto max-w-none md:max-w-4xl p-4 rounded-md
                        | grid grid-cols-1 sm:grid-cols-2 gap-6""".trimMargin()
                ) {
                    withTitle("Configuration options") {
                        checkboxGroup {
                            values(enabledGameConfigurations)
                            options = availableMoveRuleOptions
                            optionsFormat = MoveRuleOption::title
                            optionDescriptionFormat = MoveRuleOption::description
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
                                            additionalRules = it.map(MoveRuleOption::moveRule)
                                        )
                                    )
                                    close(ModalAction.Dismiss)
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
                    div("md:col-span-2") {
                        withTitle("About") {
                            iconLink(
                                CustomIcons.github,
                                text = "Sokoban",
                                description = VersionInfo.run { "$sokobanVersion ($sokobanBuildTime)" },
                                href = "https://github.com/haukesomm/sokoban"
                            )
                            iconLink(
                                CustomIcons.fritz2,
                                text = "Built with fritz2",
                                description = VersionInfo.fritz2Version,
                                href = "https://fritz2.dev"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun levelClearedAlert(game: SokobanGame) {
        modal<GameState, Unit>(game.state.filter { it.levelCleared }) {
            title("Level cleared")
            content {
                p("p-4") {
                    +"Congratulations, you successfully finished this level!"
                    br { }
                    span {
                        payload.run { +"You needed $moves moves and $pushes pushes." }
                    }
                }
            }
            closeButtonText = "Next"
            dismissButtonText = "Dismiss"
        } handledBy { result ->
            if (result.action == ModalAction.Close) {
                game.loadNextLevelIfAvailable()
            }
        }
    }

    private fun RenderContext.moveButtons(game: SokobanGame) {
        moveButtons {
            val isTouchDevice = window.asDynamic().ontouchstart != undefined || window.navigator.maxTouchPoints > 0

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

    private fun RenderContext.footer() {
        div(
            """flex flex-row flex-wrap justify-center items-center gap-x-4 gap-y-1
                | text-xs text-neutral-dark-secondary dark:text-neutral-light-secondary""".trimMargin()
        ) {
            span {
                +"© 2024 Hauke Sommerfeld"
            }
            a("flex items-center gap-1") {
                href("https://haukesomm.de")
                target("_blank")

                icon("w-3 h-3", definition = HeroIcons.link)
                +"Homepage"
            }
            span {
                +"Made with ♡ in Hamburg, Germany"
            }
        }
    }
}

fun RenderContext.gameFrame() =
    GameFrame(this).render()