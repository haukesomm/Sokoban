package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.ImmutableGameState
import de.haukesomm.sokoban.web.model.LensesGameStateDecorator
import de.haukesomm.sokoban.web.model.withLenses
import dev.fritz2.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private class TileRowLens(private val rowNumber: Int) : Lens<LensesGameStateDecorator, List<Tile>> {

    override val id: String = "tile-row-$rowNumber"

    override fun get(parent: LensesGameStateDecorator): List<Tile> =
        parent.tiles.chunked(parent.width)[rowNumber]

    override fun set(parent: LensesGameStateDecorator, value: List<Tile>): LensesGameStateDecorator =
        throw UnsupportedOperationException()
}

class GameField(states: Flow<GameState>) {

    private val gameStateStore = storeOf(
        ImmutableGameState.empty().withLenses()
    ).apply {
        states.map { it.withLenses() } handledBy update
    }
    private val gameStateHeightFlow = states.map { it.height }

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
            gameStateHeightFlow.render { height ->
                for (rowNumber in 0 until height) {
                    div("flex flex-row") {
                        val tileRowStore = gameStateStore.map(TileRowLens(rowNumber))
                        tileRowStore.data.renderEach(batch = true) {
                            renderTile(it, it.entities.firstOrNull())
                        }
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