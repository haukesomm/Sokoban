package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Tile

data class MutableGameState(
    override var levelId: String,
    override var width: Int,
    override var height: Int,
    override var tiles: MutableList<Tile>,
    override var moves: Int = 0,
    override var pushes: Int = 0,
    override var levelCleared: Boolean = false
) : AbstractGameState() {

    /**
     * Immutable property containing a combined set of all tile's entities.
     * Modify a [Tile] directly in order to change it's assigned entities.
     */
    override val entities: Set<Entity>
        get() = tiles.flatMap(Tile::entities).toMutableSet()
}

fun GameState.toMutableGameState(): MutableGameState =
    MutableGameState(
        levelId,
        width,
        height,
        tiles.toMutableList(),
        moves,
        pushes,
        levelCleared
    )

fun GameState.modify(action: MutableGameState.() -> Unit): GameState =
    this.toMutableGameState().apply(action)