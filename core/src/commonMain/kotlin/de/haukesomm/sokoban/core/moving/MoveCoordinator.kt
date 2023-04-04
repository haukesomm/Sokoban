package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.moving.validation.MoveValidator
import de.haukesomm.sokoban.core.moving.validation.MoveValidatorStatus

private data class InternalMoveCoordinatorResult(
    val success: Boolean,
    val moveActions: MutableList<MoveAction>,
    val moveValidatorStatuses: MutableSet<MoveValidatorStatus>
) {
    companion object {
        fun failure(moveValidatorStatuses: MutableSet<MoveValidatorStatus>) =
            InternalMoveCoordinatorResult(success = false, moveActions = mutableListOf(), moveValidatorStatuses)
    }
}

class MoveCoordinator(private vararg val moveValidators: MoveValidator) {

    fun moveEntityIfPossible(
        state: GameState,
        entity: Entity,
        direction: Direction
    ): MoveCoordinatorResult {
        val internalResult = determineMoveActions(state, entity, direction)

        val resultingGameState = if (internalResult.success) {
            var tmpState = state
            internalResult.moveActions.forEach { tmpState = it.performMove(tmpState) }
            tmpState
        } else {
            null
        }

        return MoveCoordinatorResult(internalResult.success, internalResult.moveValidatorStatuses, resultingGameState)
    }

    private fun determineMoveActions(state: GameState, entity: Entity, direction: Direction): InternalMoveCoordinatorResult {
        val moveValidatorStatuses = mutableSetOf<MoveValidatorStatus>().apply {
            moveValidators.forEach {
                addAll(it.check(state, entity, direction))
            }
        }

        if (MoveValidatorStatus.IMPOSSIBLE in moveValidatorStatuses) {
            return InternalMoveCoordinatorResult.failure(moveValidatorStatuses)

        } else if (MoveValidatorStatus.BOX_AHEAD_NEEDS_TO_MOVE in moveValidatorStatuses) {
            val entityAhead = state.getEntityAtNextPositionOrNull(entity.position, direction)
                ?: throw IllegalStateException("Received BOX_AHEAD_NEEDS_TO_MOVE but there is no entity!")

            return determineMoveActions(state, entityAhead, direction).also {
                if (it.success) {
                    it.moveValidatorStatuses += moveValidatorStatuses
                    it.moveActions += SimpleMoveAction(entity, direction).decorateIncrementPushes()
                }
            }
        } else {
            val moveActions = mutableListOf<MoveAction>(SimpleMoveAction(entity, direction))
            return InternalMoveCoordinatorResult(success = true, moveActions, moveValidatorStatuses)
        }
    }
}