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

fun GameState.toMutable(): MutableGameState =
    MutableGameState(
        levelId,
        width,
        height,
        tiles.toMutableList(),
        moves,
        pushes,
        levelCleared
    )

/**
 * Returns a copy of the `GameState` and applies the given [action] to it.
 * The returned state is immutable.
 */
fun GameState.transform(action: MutableGameState.() -> Unit): GameState =
    this.toMutable().apply(action).toImmutable()