package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.web.components.icons.Textures
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.*
import kotlinx.coroutines.flow.*
import org.w3c.dom.HTMLElement
import kotlin.math.floor

class GameField(private val states: Flow<GameState>) {

    fun RenderContext.render(): Tag<HTMLElement> =
        div(
            joinClasses(
                "grid rounded-lg overflow-hidden",
                "focus:outline-none focus:ring-2 focus:ring-primary-400 dark:focus:ring-primary-600"
            )
        ) {
            tabIndex(1)
            
            val computedSizes = states.flatMapLatest { state ->
                merge(
                    flowOf(state),
                    Window.resizes.map { state }
                )
            }.map { state ->
                val availableSpace = domNode.parentElement?.getBoundingClientRect()?.width ?: 0.0
                val optimalTileWidth = floor(availableSpace / state.width.toDouble()).toInt()

                state.width to optimalTileWidth
            }.distinctUntilChanged()

            className(computedSizes.take(1).map { "visible" }, initial = "hidden")

            inlineStyle(
                computedSizes.map { (gameBoardWidth, tileWidth) ->
                    """
                        width: ${gameBoardWidth * tileWidth}px;
                        grid-template-columns: repeat($gameBoardWidth, ${tileWidth}px);
                    """.trimIndent()
                }
            )

            states.map { it.tiles }.renderEach {
                renderTile(it)
            }
        }

    private fun RenderContext.renderTile(tile: Tile) =
        div("w-full h-auto overflow-visible") {
            icon("w-full h-auto overflow-visible", definition = tile.entity?.let { entity ->
                when(entity.type) {
                    EntityType.Box -> Textures.box
                    EntityType.Player -> Textures.player
                }
            } ?: run {
                when(tile.type) {
                    TileType.Empty -> Textures.ground
                    TileType.Target -> Textures.target
                    TileType.Wall -> Textures.wall
                }
            })
        }
}

fun RenderContext.gameField(states: Flow<GameState>): Tag<HTMLElement> =
    GameField(states).run {
        render()
    }