package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.SokobanGame
import de.haukesomm.sokoban.core.level.LevelDescription
import de.haukesomm.sokoban.core.level.bundled.BundledLevelRepository
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.web.components.*
import de.haukesomm.sokoban.web.components.game.gameField
import de.haukesomm.sokoban.web.components.icons.Fritz2Icons
import de.haukesomm.sokoban.web.components.icons.GitHubIcons
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import de.haukesomm.sokoban.web.theme.ThemePreference
import de.haukesomm.sokoban.web.theme.ThemePreferences
import dev.fritz2.core.*
import dev.fritz2.headless.components.disclosure
import kotlinx.coroutines.flow.*

class GameFrame {

    companion object {
        private const val MAX_TITLEBAR_WIDTH_CLASSES = "max-w-4xl"
    }

    private val enabledRulesStore = storeOf(UserSelectableRules.rules)

    private val game = SokobanGame(
        BundledLevelRepository(),
        enabledRulesStore.data.map { it.rules }
    )

    private val levelDescriptions = game.getAvailableLevels()

    private val selectedLevelStore = storeOf(levelDescriptions.first())


    init {
        selectedLevelStore.data handledBy {
            game.loadLevel(it.id)
        }
    }


    private fun RenderContext.initializeKeyboardInput() {
        Window.keydowns.map { shortcutOf(it.key) } handledBy { shortcut ->
            when(shortcut) {
                Keys.ArrowUp -> game.movePlayerIfPossible(Direction.Top)
                Keys.ArrowDown -> game.movePlayerIfPossible(Direction.Bottom)
                Keys.ArrowLeft -> game.movePlayerIfPossible(Direction.Left)
                Keys.ArrowRight -> game.movePlayerIfPossible(Direction.Right)
            }
        }
    }


    private fun RenderContext.initializeLevelClearedAlert() {
        game.state.let { state ->
            state.filter { it.levelCleared }.map { } handledBy levelClearedAlert(state)
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


    fun RenderContext.render() {
        initializeKeyboardInput()
        initializeLevelClearedAlert()

        div("mb-4 w-full flex flex-col gap-4 items-center") {
            titleBar()

            div("max-w-min rounded-lg shadow overflow-hidden") {
                gameField(game.state)
            }
        }
    }

    private fun RenderContext.titleBar() {
        disclosure("w-full py-2 px-4 flex flex-col items-center bg-background-lightest dark:bg-background-dark shadow-sm dark:shadow-md") {
            div(
                classes(
                    "w-full flex flex-row justify-between items-center gap-4 text-sm",
                    MAX_TITLEBAR_WIDTH_CLASSES
                )
            ) {
                span("text-xl font-semibold text-primary-500 dark:text-primary-600") {
                    +"Sokoban"
                }
                div("grow max-w-md flex gap-6 items-center") {
                    div("grow") {
                        listBox {
                            entries = levelDescriptions
                            format = LevelDescription::name
                            value(selectedLevelStore)
                        }
                    }
                    span {
                        +"Moves: "
                        code {
                            game.state.map { it.moves }.render(into = this) {
                                +it.toString()
                            }
                        }
                    }
                    span {
                        +"Pushes: "
                        code {
                            game.state.map { it.pushes }.render(into = this) {
                                +it.toString()
                            }
                        }
                    }
                    plainButton {
                        iconDefinition(HeroIcons.refresh)
                        iconSize = PlainButton.IconSize.Small
                        text("Reload")
                    }.run {
                        title("Reload level")
                        clicks handledBy {
                            game.reloadLevel()
                        }
                    }
                }
                disclosureButton(
                    """p-1 flex flex-row gap-2 items-center rounded-sm focus:outline-none
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
            disclosurePanel {
                div(
                    classes(
                        """mt-4 my-2 p-4 bg-background-light dark:bg-background-darkest rounded-md
                            | grid grid-cols-3 gap-6""".trimMargin(),
                        MAX_TITLEBAR_WIDTH_CLASSES
                    )
                ) {
                    withTitle("Rules") {
                        checkboxGroup {
                            values(enabledRulesStore)
                            options = UserSelectableRules.rules
                            optionsFormat = MoveRuleWithDescription::title
                            optionDescriptionFormat = MoveRuleWithDescription::description
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
                                | border-dotted border-neutral-dark-secondary dark:border-neutral-light-secondary""".trimMargin()
                        ) {
                            +"Made with ♡ in Hamburg, Germany"
                        }
                    }
                }
            }
        }
    }
}

fun RenderContext.gameFrame() =
    GameFrame().run { render() }