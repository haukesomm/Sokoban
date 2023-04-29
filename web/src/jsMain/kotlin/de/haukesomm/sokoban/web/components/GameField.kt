package de.haukesomm.sokoban.web.components

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.ImmutableGameState
import de.haukesomm.sokoban.web.model.LensesGameStateDecorator
import de.haukesomm.sokoban.web.model.tiles
import de.haukesomm.sokoban.web.model.withLenses
import dev.fritz2.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GameField(states: Flow<GameState>) {

    private val gameStateStore = storeOf(
        ImmutableGameState.empty().withLenses()
    ).apply {
        states.map { it.withLenses() } handledBy update
    }
    private val gameStateTilesFlow = gameStateStore.map(LensesGameStateDecorator.tiles()).data

    private fun getTileTexture(tileType: Tile.Type) = when(tileType) {
        Tile.Type.Empty -> "ground.png"
        Tile.Type.Wall -> "wall.png"
        Tile.Type.Target -> "target.png"
    }

    private fun getEntityTexture(entity: Entity) = when(entity.type) {
        Entity.Type.Box -> "box.png"
        Entity.Type.Player -> "player.png"
    }

    fun RenderContext.render() {
        div {
            gameStateStore.data.map { it.width }.render(into = this) { width ->
                div("grid") {
                    // Inline-style is needed since levels may have arbitrary, dynamically changing dimensions that
                    // can't be realized with pure Tailwind CSS (not even in JIT mode).
                    inlineStyle("grid-template-columns: repeat($width, 1fr);")

                    gameStateTilesFlow.renderEach(batch = true) {
                        renderTile(it, it.entities.firstOrNull())
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