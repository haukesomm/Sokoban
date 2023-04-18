package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.web.components.icons.icon
import dev.fritz2.core.RenderContext
import dev.fritz2.core.src
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Image

class GameField(private val states: Flow<GameState>) {

    private fun getTileTexture(tileType: TileType) = when(tileType) {
        TileType.NOTHING -> "ground.png"
        TileType.WALL -> "wall.png"
        TileType.TARGET -> "target.png"
    }

    private fun getEntityTexture(entity: Entity) = when(entity.type) {
        EntityType.BOX -> "box.png"
        EntityType.PLAYER -> "player.png"
    }

    fun RenderContext.render() {
        div {
            states.render(into = this) { state ->
                for (y in 0 until state.height) {
                    div("flex flex-row") {
                        for (x in 0 until state.width) {
                            val tile = state.tileAt(x, y)!!
                            val entity = state.entityAt(x, y)
                            renderTile(tile, entity)
                        }
                    }
                }
            }
        }
    }

    private fun RenderContext.renderTile(tile: Tile, entity: Entity?) {
        img("min-w-min min-h-min fit-picture") {
            val baseTexturePath = "./textures/"

            entity?.let(::getEntityTexture)?.let {
                src("$baseTexturePath/$it")
                return@img
            }

            src("$baseTexturePath/${getTileTexture(tile.type)}")
        }
    }
}

fun RenderContext.gameField(states: Flow<GameState>, ) {
    GameField(states).run {
        render()
    }
}