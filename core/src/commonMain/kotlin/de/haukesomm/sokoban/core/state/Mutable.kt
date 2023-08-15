package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.Tile
import kotlinx.serialization.Serializable

/**
 * Mutable implementation of [GameState].
 *
 * This implementation allows modifications to the game state.
 * An immutable copy of the game state can be created via the [toImmutable] method.
 */
@Serializable
data class MutableGameState(
    override var levelId: String,
    override var width: Int,
    override var height: Int,
    override var tiles: MutableList<Tile>,
    override var moves: Int = 0,
    override var pushes: Int = 0,
    override var levelCleared: Boolean = false
) : GameState

/**
 * Creates a mutable copy of this [GameState].
 */
fun GameState.toMutable(): MutableGameState =
    MutableGameState(levelId, width, height, tiles.toMutableList(), moves, pushes, levelCleared)

/**
 * Returns a copy of the `GameState` and applies the given [action] to it.
 * The returned state is immutable.
 */
fun GameState.transform(action: MutableGameState.() -> Unit): GameState =
    this.toMutable().apply(action).toImmutable()