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
fun interface MoveAction {

    /**
     * Performs the move on the given [state] and returns the new [GameState].
     */
    fun performMove(state: GameState): GameState
}

/**
 * Represents a basic move of an [Entity] at a given [position] in a given [direction].
 *
 * The move is performed by removing the entity from the current position and adding it to the next position.
 *
 * The moves and pushes counters are not incremented when using this action. Instead, use [movesIncrementing] or
 * [pushesIncrementing] decorator methods depending on the type of move.
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
        }
}

/**
 * Convenience method decorating a [MoveAction] so that the [GameState.moves] counter is incremented after the
 * decorated move is performed.
 */
fun MoveAction.movesIncrementing(): MoveAction =
    MoveAction { state ->
        performMove(state).transform { moves++ }
    }

/**
 * Convenience method decorating a [MoveAction] so that the [GameState.pushes] counter is incremented after the
 * decorated move is performed.
 */
fun MoveAction.pushesIncrementing(): MoveAction =
    MoveAction { state ->
        performMove(state).transform { pushes++ }
    }
