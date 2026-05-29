package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.*

class StateMachineBasedBasedMoveService : MoveService {

    override fun moveEntityIfPossible(
        state: GameState,
        position: Position,
        direction: Direction
    ): GameState? {
        val current = state.tileAt(position)
            ?: return run {
                println("Warning: Attempting to move from a Tile that does not exist!")
                null
            }
        val next = state.tileAt(position.nextInDirection(direction))
            ?: return run {
                println("Warning: Attempting to move to a Tile that does not exist!")
                null
            }

        return when (val result = StateMachine.computeTransition(current, next)) {
            StateMachine.TransitionResult.Abort -> null

            StateMachine.TransitionResult.LookAhead -> moveEntityIfPossible(
                state,
                position.nextInDirection(direction),
                direction,
            )?.let { newState ->
                val reComputeResult = StateMachine.computeTransition(
                    newState.tileAt(position)!!,
                    newState.tileAt(position.nextInDirection(direction))!!
                )
                when (reComputeResult) {
                    is StateMachine.TransitionResult.Transform -> newState
                        .applyTransformation(position, direction, reComputeResult)
                        .incrementPushes()

                    else -> null
                }
            }

            is StateMachine.TransitionResult.Transform -> state
                .applyTransformation(position, direction, result)
                .incrementMoves()
        }
    }
}

private fun GameState.applyTransformation(
    position: Position,
    direction: Direction,
    transformation: StateMachine.TransitionResult.Transform
): GameState = transform {
    previous = this@applyTransformation

    val currentIndex = position.toIndex(width)
    val nextIndex = position.nextInDirection(direction).toIndex(width)

    val (newCurrent, newNext) = transformation

    tiles[currentIndex] = newCurrent
    tiles[nextIndex] = newNext
}

private fun GameState.incrementMoves(): GameState = transform {
    moves++
}

private fun GameState.incrementPushes(): GameState = transform {
    pushes++
}
