package de.haukesomm.sokoban.core.model

import de.haukesomm.sokoban.core.model.Tile.PlayerOnGround
import de.haukesomm.sokoban.core.model.Tile.PlayerOnTarget
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents the state of a game.
 *
 * The state contains the id of the level, the width and height of the game board, the tiles of the game board,
 * the number of moves and pushes the player has made and whether the level has been cleared or not.
 *
 * A number of convenience methods are provided to access the tiles and entities on the game board.
 */
interface GameState {

    /**
     * Unique identifier of the level.
     */
    val levelId: String

    /**
     * Width of the game board.
     */
    val width: Int

    /**
     * Height of the game board.
     */
    val height: Int

    /**
     * List of all tiles on the game board. The length must be equal to [width] * [height].
     */
    val tiles: List<Tile>

    /**
     * Number of moves the player has made.
     */
    val moves: Int

    /**
     * Number of pushes the player has made.
     */
    val pushes: Int

    /**
     * Returns the previous [GameState] or `null` if this is the first state.
     *
     * By traversing the game state tree, it is possible to implement features like undo/redo, replay or a visual
     * representation of the game state history.
     */
    val previous: GameState?
}


/**
 * `true` if the level has been cleared, `false` otherwise.
 */
val GameState.levelCleared: Boolean
    get() = tiles.none { it == Tile.BoxOnGround }


/**
 * Returns the [Tile] at the given [position] or `null` if the position is out of bounds.
 */
fun GameState.tileAt(position: Position): Tile? =
    tiles.getOrNull(position.toIndex(width))

/**
 * Returns the next [Tile] in the given [direction] or `null` if the next position is out of bounds.
 */
fun GameState.tileInDirection(position: Position, direction: Direction): Tile? =
    tileAt(position.nextInDirection(direction))

/**
 * Returns the player's [Position] or `null` if the player is not on the game board.
 */
fun GameState.getPlayerPosition(): Position? =
    when (val index = tiles.indexOfFirst { it in setOf(PlayerOnGround, PlayerOnTarget) }) {
        -1 -> null
        else -> Position.fromIndex(index, width)
    }

/**
 * Mutable implementation of [GameState].
 *
 * This implementation allows modifications to the game state.
 */
data class MutableGameState(
    override var levelId: String,
    override var width: Int,
    override var height: Int,
    override var tiles: MutableList<Tile>,
    override var moves: Int = 0,
    override var pushes: Int = 0,
    override var previous: GameState? = null
) : GameState

/**
 * Creates a mutable copy of this [GameState].
 */
fun GameState.createMutableCopy(): MutableGameState =
    MutableGameState(levelId, width, height, tiles.toMutableList(), moves, pushes, previous)

/**
 * Returns a copy of the `GameState` and transforms if by applying the given [action] to it.
 */
fun GameState.transform(action: MutableGameState.() -> Unit): GameState =
    this.createMutableCopy().apply(action)
