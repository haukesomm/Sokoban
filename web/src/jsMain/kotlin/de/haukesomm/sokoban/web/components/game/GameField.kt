package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.Tile
import de.haukesomm.sokoban.core.TileType
import de.haukesomm.sokoban.web.components.icons.Textures
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GameField(private val states: Flow<GameState>) {

    companion object {
        private const val TILE_SIZE_CLASSES = "w-5 h-5 md:w-8 md:h-8"
    }

    fun RenderContext.render() {
        div("grid") {
            inlineStyle(states.map {
                "grid-template-columns: repeat(${it.width}, 1fr);"
            })
            states.map { it.tiles }.renderEach( batch = true) {
                renderTile(it)
            }
        }
    }

    private fun RenderContext.renderTile(tile: Tile) =
        div {
            tile.entity?.let { entity ->
                icon(TILE_SIZE_CLASSES, definition = when(entity.type) {
                    EntityType.Box -> Textures.box
                    EntityType.Player -> Textures.player
                })
            } ?: run {
                icon(TILE_SIZE_CLASSES, definition = when(tile.type) {
                    TileType.Empty -> Textures.ground
                    TileType.Target -> Textures.target
                    TileType.Wall -> Textures.wall
                })
            }
        }
}

fun RenderContext.gameField(states: Flow<GameState>, ) {
    GameField(states).run {
        render()
    }
}