package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
import kotlin.jvm.JvmStatic

private data class InternalMoveCoordinatorResult(
    val success: Boolean,
    val moveActions: MutableList<MoveAction>,
    val moveRuleResults: MutableSet<MoveRuleResult>
) {
    companion object {
        fun failure(moveRuleResults: MutableSet<MoveRuleResult>) =
            InternalMoveCoordinatorResult(success = false, moveActions = mutableListOf(), moveRuleResults)
    }
}

class MoveCoordinator(vararg moveRules: MoveRule) {

    private val moveRuleList = moveRules.toSet()

    fun moveEntityIfPossible(
        state: GameState,
        entity: Entity,
        direction: Direction
    ): GameState? {
        val internalResult = determineMoveActions(state, entity, direction)

        val resultingGameState = if (internalResult.success) {
            var tmpState = state
            internalResult.moveActions.forEach { tmpState = it.performMove(tmpState) }
            tmpState
        } else {
            null
        }

        return resultingGameState
    }

    private fun determineMoveActions(state: GameState, entity: Entity, direction: Direction): InternalMoveCoordinatorResult {
        val moveRuleResults = moveRuleList
            .checkAll(state, entity, direction)
            .toMutableSet()

        if (MoveRuleResult.Impossible in moveRuleResults) {
            return InternalMoveCoordinatorResult.failure(moveRuleResults)

        } else if (MoveRuleResult.BoxAheadNeedsToMove in moveRuleResults) {
            val entityAhead = state.nextEntityInDirection(entity.position, direction)
                ?: throw IllegalStateException("Box ahead needs to move but there is no entity!")

            return determineMoveActions(state, entityAhead, direction).also {
                if (it.success) {
                    it.moveRuleResults += moveRuleResults
                    it.moveActions += PushesIncrementingMoveActionDecorator(
                        SimpleMoveAction(entity, direction)
                    )
                }
            }
        } else {
            val moveActions = mutableListOf<MoveAction>(SimpleMoveAction(entity, direction))
            return InternalMoveCoordinatorResult(success = true, moveActions, moveRuleResults)
        }
    }

    companion object {
        @JvmStatic
        fun withDefaultRules(): MoveCoordinator =
            MoveCoordinator(
                ConditionalMoveRule(
                    condition = OutOfBoundsPreventingMoveRule(),
                    moveRules = setOf(
                        WallCollisionPreventingMoveRule(),
                        BoxDetectingMoveRule(),
                        MultipleBoxesPreventingMoveRule()
                    )
                )
            )
    }
}