package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Service for moving entities in a [GameState].
 *
 * The service provides a [moveEntityIfPossible] method which tries to move an entity in a given direction.
 * If the move is possible, the method returns a new [GameState] with the entity moved. Otherwise, the method
 * returns `null`.
 *
 * In order to do so, the service uses a set of [moveRules]. These rules are checked in order and the first rule
 * which returns [MoveRuleResult.Impossible] stops the process and the method returns `null`. If the first rule
 * returns [MoveRuleResult.BoxAheadNeedsToMove], the service tries to move the box ahead recursively. If this
 * recursive call returns `null`, the method returns `null` as well. Otherwise, the method returns a new
 * [GameState] with the entity moved and the box moved as well.
 */
class MoveService(private vararg val moveRules: MoveRule) {

    companion object {

        /**
         * Creates a new [MoveService] with a set of default rules.
         *
         * These include basic necessary rules to prevent the user from moving entities out of the game board,
         * prevent the user from moving entities after the game has been completed and enables the user to push
         * boxes.
         *
         * Additional rules can be passed via the optional [additionalRules] parameter.
         */
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

    /**
     * Tries to move the entity at the given [position] in the given [direction] in the given [state].
     *
     * The method returns `null` if the move is not possible. Otherwise, the method returns a new [GameState].
     */
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