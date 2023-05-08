package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState

class MoveCoordinator(vararg moveRules: MoveRule) {
    private val moveRuleSet = moveRules.toSet()

    private data class Result(
        val success: Boolean,
        val moveActions: MutableList<MoveAction>
    )

    fun moveEntityIfPossible(state: GameState, entity: Entity, direction: Direction): GameState? {
        val internalResult = determineMovesRecursively(state, entity, direction)

        return if (internalResult.success) {
            var tmpState = state
            internalResult.moveActions.forEach { tmpState = it.performMove(tmpState) }
            tmpState
        } else {
            null
        }
    }

    private fun determineMovesRecursively(state: GameState, entity: Entity, direction: Direction): Result {
        val moveRuleResults = moveRuleSet
            .checkAll(state, entity, direction)
            .toMutableSet()

        if (MoveRuleResult.Impossible in moveRuleResults) {
            return Result(
                success = false,
                moveActions = mutableListOf()
            )
        } else if (MoveRuleResult.BoxAheadNeedsToMove in moveRuleResults) {
            val entityAhead = state.nextEntityInDirection(entity.position, direction)
                ?: throw IllegalStateException("Box ahead needs to move but there is no entity!")

            return determineMovesRecursively(state, entityAhead, direction).apply {
                if (success) {
                    moveActions += PushesIncrementingMoveActionDecorator(
                        SimpleMoveAction(entity, direction)
                    )
                }
            }
        } else {
            return Result(
                success = true,
                moveActions = mutableListOf(SimpleMoveAction(entity, direction))
            )
        }
    }
}