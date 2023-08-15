package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform

/**
 * Represents an action that can be performed on a [GameState].
 *
 * Typically, this is a move of the player or a box. The action is performed on a [GameState] and returns a _new_
 * [GameState] with the move applied.
 */
interface MoveAction {

    /**
     * Performs the move on the given [state] and returns the new [GameState].
     */
    fun performMove(state: GameState): GameState
}

/**
 * Represents a basic move of an [Entity] at a given [position] in a given [direction].
 *
 * The move is performed by removing the entity from the current position and adding it to the next position.
 * Once the move is performed, the [GameState.moves] counter is incremented. This is done regardless of whether
 * a box has been moved or not after every turn the player makes.
 *
 * Box pushes are counted separately and are only incremented if a box has been moved.
 *
 * @see PushesIncrementingMoveActionDecorator
 */
class SimpleMoveAction(
    private val position: Position,
    private val direction: Direction
) : MoveAction {

    override fun performMove(state: GameState): GameState =
        state.transform {
            val currentIndex = position.toIndex(state.width)
            val entity = tiles[currentIndex].entity
            tiles[currentIndex] = tiles[currentIndex].copy(entity = null)

            val nextIndex = position.nextInDirection(direction).toIndex(state.width)
            tiles[nextIndex] = tiles[nextIndex].copy(entity = entity)

            moves++
        }
}

/**
 * Decorator for a [MoveAction] that increments the [GameState.pushes] counter after the move is performed.
 */
class PushesIncrementingMoveActionDecorator(private val decorated: MoveAction) : MoveAction {

    override fun performMove(state: GameState): GameState =
        decorated.performMove(state).transform { pushes++ }
}
