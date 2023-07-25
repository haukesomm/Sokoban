package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

class MoveService(private vararg val moveRules: MoveRule) {

    companion object {
        @JvmStatic
        @JvmOverloads
        fun withDefaultRules(additionalRules: Collection<MoveRule> = emptyList()): MoveService =
            MoveService(
                ConditionalMoveRule(
                    condition = AggregatingMoveRule(
                        OutOfBoundsPreventingMoveRule(),
                        MoveAfterCompletionPreventingMoveRule()
                    ),
                    moveRules = setOf(
                        BoxDetectingMoveRule(),
                        *additionalRules.toTypedArray()
                    )
                )
            )
    }

    private data class Result(
        val success: Boolean,
        val moveActions: MutableList<MoveAction>
    )

    fun moveEntityIfPossible(state: GameState, position: Position, direction: Direction): GameState? {
        val internalResult = determineMovesRecursively(state, position, direction)

        return if (internalResult.success) {
            var tmpState = state
            internalResult.moveActions.forEach { tmpState = it.performMove(tmpState) }
            tmpState
        } else {
            null
        }
    }

    private fun determineMovesRecursively(state: GameState, position: Position, direction: Direction): Result {
        val moveRuleResults = moveRules
            .checkAll(state, position, direction)
            .toMutableSet()

        if (MoveRuleResult.Impossible in moveRuleResults) {
            return Result(
                success = false,
                moveActions = mutableListOf()
            )
        } else if (MoveRuleResult.BoxAheadNeedsToMove in moveRuleResults) {
            val nextPosition = position.nextInDirection(direction)

            if (state.tileAt(nextPosition)?.entity == null)
                throw IllegalStateException("Box ahead needs to move but there is no entity!")

            return determineMovesRecursively(state, nextPosition, direction).apply {
                if (success) {
                    moveActions += PushesIncrementingMoveActionDecorator(
                        SimpleMoveAction(position, direction)
                    )
                }
            }
        } else {
            return Result(
                success = true,
                moveActions = mutableListOf(SimpleMoveAction(position, direction))
            )
        }
    }
}