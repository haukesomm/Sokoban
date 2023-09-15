package de.haukesomm.sokoban.web.components.game

import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.Tile
import de.haukesomm.sokoban.core.TileType
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.web.model.LensesSupportingGameState
import de.haukesomm.sokoban.web.model.tiles
import de.haukesomm.sokoban.web.model.toLensesSupporting
import dev.fritz2.core.RenderContext
import dev.fritz2.core.src
import dev.fritz2.core.storeOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameField(states: Flow<GameState>) {

    private val gameStateStore = storeOf(LensesSupportingGameState("", 0, 0, emptyList())).apply {
        states.map { it.toLensesSupporting() } handledBy update
    }
    private val gameStateTilesFlow = gameStateStore.map(LensesSupportingGameState.tiles()).data

    private fun getTileTexture(tileType: TileType) = when(tileType) {
        TileType.Empty -> "ground.png"
        TileType.Wall -> "wall.png"
        TileType.Target -> "target.png"
    }

    private fun getEntityTexture(entity: Entity) = when(entity.type) {
        EntityType.Box -> "box.png"
        EntityType.Player -> "player.png"
    }

    fun RenderContext.render() {
        div {
            gameStateStore.data.map { it.width }.render(into = this) { width ->
                div("grid") {
                    // Inline-style is needed since levels may have arbitrary, dynamically changing dimensions that
                    // can't be realized with pure Tailwind CSS (not even in JIT mode).
                    inlineStyle("grid-template-columns: repeat($width, 1fr);")

                    gameStateTilesFlow.renderEach(batch = true) {
                        renderTile(it, it.entity)
                    }
                }
            }
        }
    }

    private fun RenderContext.renderTile(tile: Tile, entity: Entity?) =
        div("w-7 h-7") {
            img("fit-picture") {
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