package de.haukesomm.sokoban.core

/**
 * Represents the state of a game.
 *
 * The state contains the id of the level, the width and height of the game board, the tiles of the game board,
 * the number of moves and pushes the player has made and whether the level has been cleared or not.
 *
 * A number of convenience methods are provided to access the tiles and entities on the game board.
 *
 * There may be several implementations of this interface. The two fundamental implementations provided by this
 * library are [ImmutableGameState] and [MutableGameState].
 *
 * Other implementations may be provided in order to support different use cases. For example, a [GameState] may
 * be implemented to work with other frameworks mechanics such as fritz2's `@Lenses` annotation.
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
    get() = tiles.none { tile ->
        tile.isTarget && tile.entity?.takeIf { it.isBox } == null
    }


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
 * Returns the [Entity] at the given [position] or `null` if the position is out of bounds.
 */
fun GameState.entityAt(position: Position): Entity? =
    tileAt(position)?.entity

/**
 * Returns the next [Entity] in the given [direction] or `null` if the next position is out of bounds.
 */
fun GameState.entityInDirection(position: Position, direction: Direction): Entity? =
    entityAt(position.nextInDirection(direction))

/**
 * Returns the player's [Position] or `null` if the player is not on the game board.
 */
fun GameState.getPlayerPosition(): Position? =
    when (val index = tiles.indexOfFirst { it.entity?.type == EntityType.Player }) {
        -1 -> null
        else -> Position.fromIndex(index, width)
    }


/**
 * Immutable implementation of [GameState].
 *
 * This implementation does not allow modifications to the game state.
 * A mutable copy of the game state can be created via the [toMutableGameState] method.
 */
data class ImmutableGameState(
    override val levelId: String,
    override val width: Int,
    override val height: Int,
    override val tiles: List<Tile>,
    override val moves: Int = 0,
    override val pushes: Int = 0,
    override val previous: GameState? = null
) : GameState {
    companion object {
        /**
         * Returns an [ImmutableGameState] converted from the given [level].
         */
        fun fromLevel(level: Level): ImmutableGameState =
            with(level) {
                val tiles = normalizedLayoutString.map { character ->
                    characterMap
                        .getOrElse(character) { TileProperties(TileType.Empty) }
                        .run(Tile::fromTileProperties)
                }
                ImmutableGameState(id, width, height, tiles)
            }
    }
}

/**
 * Creates a mutable copy of this [GameState].
 */
fun GameState.toImmutableGameState(): ImmutableGameState =
    if (this is ImmutableGameState) this
    else ImmutableGameState(levelId, width, height, tiles, moves, pushes, previous)


/**
 * Mutable implementation of [GameState].
 *
 * This implementation allows modifications to the game state.
 * An immutable copy of the game state can be created via the [toImmutableGameState] method.
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
fun GameState.toMutableGameState(): MutableGameState =
    MutableGameState(levelId, width, height, tiles.toMutableList(), moves, pushes, previous)

/**
 * Returns a copy of the `GameState` and applies the given [action] to it.
 * The returned state is immutable.
 */
fun GameState.transform(action: MutableGameState.() -> Unit): GameState =
    this.toMutableGameState().apply(action).toImmutableGameState()
