package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.Tile
import de.haukesomm.sokoban.core.TileType
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.web.components.icons.Textures
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameField(private val states: Flow<GameState>) {

    companion object {
        private const val TILE_SIZE_CLASSES = "w-8 h-8"
    }

    fun RenderContext.render() {
        div {
            states.map { it.width to it.tiles }.render(into = this) { (width, tiles) ->
                div("grid") {
                    inlineStyle("grid-template-columns: repeat($width, 1fr);")
                    tiles.forEach {
                        renderTile(it)
                    }
                }
            }
        }
    }

    private fun RenderContext.renderTile(tile: Tile) {
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