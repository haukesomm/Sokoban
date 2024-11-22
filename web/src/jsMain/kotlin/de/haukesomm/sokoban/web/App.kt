package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.web.components.*
import de.haukesomm.sokoban.web.components.game.gameField
import de.haukesomm.sokoban.web.components.game.moveButtons
import de.haukesomm.sokoban.web.components.icons.CustomIcons
import de.haukesomm.sokoban.web.components.icons.HeroIcons
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.*
import dev.fritz2.headless.foundation.portalRoot
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

fun main() {
    render {
        app()
        portalRoot()
    }
}


private fun RenderContext.app() {
    val sokobanGame = SokobanGameFactory.withDefaultConfiguration()

    main("mb-4 flex flex-col justify-between items-center gap-2 sm:gap-8") {
        div("self-stretch") {
            headerBar(sokobanGame)
        }

        div(
            joinClasses(
                // If the device is a touch screen device and larger than approximately a phone, we want to show the
                // move buttons on the sides of the game field. For this purpose, the following special grid sub-layout
                // kicks in. Otherwise, the normal layout is followed. See 'touch-only-subgrid' in the main CSS file.
                "mx-4 touch-only-subgrid sm:self-stretch",
                "sm:grid-cols-[minmax(max-content,_auto)_minmax(0,_1fr)_minmax(max-content,_auto)]",
                "sm:justify-items-center sm:items-center sm:gap-4"
            )
        ) {
            div("hidden sm:block touch-only") {
                moveButtons() handledBy {
                    sokobanGame.movePlayerIfPossible(it)
                }
            }

            div("w-full max-w-2xl flex justify-center") {
                gameField(sokobanGame.state)
            }

            div("touch-only") {
                moveButtons() handledBy {
                    sokobanGame.movePlayerIfPossible(it)
                }
            }
        }

        handKeyboardInput(sokobanGame)
        showDialogOnLevelCompletion(sokobanGame)
    }
}

private fun RenderContext.headerBar(game: SokobanGame) {
    div(
        joinClasses(
            "p-3 shadow bg-background",
            "flex flex-row flex-wrap items-center gap-4 md:justify-between"
        )
    ) {
        sokobanTitleAndIcon()


        div("contents md:flex flex-wrap items-center gap-4") {
            val levels = game.getAvailableLevels()
            val selectedLevel = storeOf(levels.first())

            // Load level when a new level is selected
            selectedLevel.data handledBy {
                if (it.id != game.currentState.levelId) {
                    game.loadLevel(it.id)
                }
            }

            // Select the correct level when the loaded level changes
            game.state.map { it.levelId }.mapNotNull { id ->
                if (id != selectedLevel.current.id) levels.find { it.id == id }
                else null
            } handledBy selectedLevel.update

            div("w-full md:w-auto md:min-w-48") {
                comboBox<LevelDescription> {
                    options = levels
                    optionsFormat = LevelDescription::name
                    value(selectedLevel)
                }
            }


            with(game.state) {
                map { "Moves: ${it.moves}" }.renderText()
                map { "Pushes: ${it.pushes}" }.renderText()
            }


            textButton("Undo", HeroIcons.reply, disabled = game.state.map { it.previous == null }) handledBy {
                game.undoLastMoveIfPossible()
            }

            textButton("Restart", HeroIcons.refresh) handledBy {
                game.reloadLevel()
            }
        }


        popOver {
            label("About")
            content {
                about()
            }
        }
    }
}

private fun RenderContext.sokobanTitleAndIcon() {
    div("flex items-center gap-2") {
        icon("w-6 h-6", definition = CustomIcons.sokoban)
        span("font-semibold bg-marker-light dark:bg-marker-dark") {
            +"Sokoban"
        }
    }
}

private fun RenderContext.about() {
    div("flex flex-col gap-3") {
        iconLink(
            CustomIcons.github,
            text = "Sokoban",
            description = VersionInfo.sokobanVersion,
            hint = with(VersionInfo) { "Version $sokobanVersion, built on $sokobanBuildTime" },
            href = "https://github.com/haukesomm/sokoban"
        )
        iconLink(
            CustomIcons.fritz2,
            text = "Built with fritz2",
            description = VersionInfo.fritz2Version,
            href = "https://fritz2.dev"
        )
        iconLink(
            HeroIcons.globe,
            text = "Visit my website",
            description = "https://haukesomm.de",
            href = "https://haukesomm.de"
        )
        div("pt-3 border-t border-background-accent text-xs font-sans") {
            +"Made with ♡ in Hamburg, Germany"
        }
    }
}

private fun Tag<*>.handKeyboardInput(game: SokobanGame) {
    Window.keydowns.mapNotNull {
        when (shortcutOf(it.key)) {
            Keys.ArrowUp -> Direction.Top
            Keys.ArrowDown -> Direction.Bottom
            Keys.ArrowLeft -> Direction.Left
            Keys.ArrowRight -> Direction.Right
            else -> null
        }
    } handledBy {
        game.movePlayerIfPossible(it)
    }
}

private fun RenderContext.showDialogOnLevelCompletion(game: SokobanGame) {
    dialog(game.state.filter { it.levelCleared }) {
        content {
            div("flex flex-col gap-4 max-w-lg") {
                p("font-semibold") {
                    +"Level cleared!"
                }
                p {
                    +"""Congratulations! You have completed the level in ${payload.moves} moves and
                        | ${payload.pushes} pushes.""".trimMargin()
                }
                div("flex gap-4 justify-end items-center") {
                    textButton("Restart", HeroIcons.refresh) handledBy {
                        game.reloadLevel()
                        close(Unit)
                    }
                    textButton("Next level", HeroIcons.arrow_right) handledBy {
                        game.loadNextLevelIfAvailable()
                        close(Unit)
                    }
                }
            }
        }
    }
}