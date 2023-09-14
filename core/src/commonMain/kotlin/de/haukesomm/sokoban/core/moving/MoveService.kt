package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform
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
         * A set of recommended rules to use with a [MoveService].
         *
         * These rules are optional, yet recommended, in order to provide an experience similar to the original game.
         *
         * When using the recommended [MoveService.withRecommendedRules] method, these rules are used by default.
         * When using [MoveService.withMinimalRules], these rules can be passed as an additional parameter.
         */
        val recommendedRules: List<UserSelectableMoveRule> =
            listOf(
                WallCollisionPreventingMoveRule(),
                MultipleBoxesPreventingMoveRule()
            )

        /**
         * Creates a new [MoveService] with a set of default rules.
         *
         * These include basic necessary rules to prevent the user from moving entities out of the game board,
         * prevent the user from moving entities after the game has been completed and enables the user to push
         * boxes. Additional rules can be passed via the [additional] parameter.
         *
         * The main purpose of this method is to provide a basic mechanism for user-customizable rules.
         * Just pass your own rules as an additional parameter. A good start would be to let the user choose from
         * the rules defined in [recommendedRules].
         * In case you did implement custom rules, you can pass them to this method as well.
         *
         * __In most cases, it is recommended to use [withRecommendedRules] over this method!__
         */
        @JvmStatic
        @JvmOverloads
        fun withMinimalRules(additional: Collection<MoveRule> = emptyList()): MoveService =
            MoveService(
                ConditionalMoveRule(
                    condition = AggregatingMoveRule(
                        OutOfBoundsPreventingMoveRule(),
                        MoveAfterCompletionPreventingMoveRule()
                    ),
                    moveRules = setOf(
                        BoxDetectingMoveRule(),
                        *additional.toTypedArray()
                    )
                )
            )

        /**
         * Creates a new [MoveService] with a set of recommended rules.
         *
         * These include the rules from [withMinimalRules] and the rules from [recommendedRules].
         */
        @JvmStatic
        fun withRecommendedRules(): MoveService =
            withMinimalRules(additional = recommendedRules)
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
        val result = determineMovesRecursively(state, position, direction)

        return if (result.success) {
            result.moveActions
                .fold(state) { acc, action -> action.performMove(acc) }
                .transform { previous = state }
        } else null
    }

    private fun determineMovesRecursively(state: GameState, position: Position, direction: Direction): Result {
        val results = moveRules
            .toSet()
            .checkAll(state, position, direction)

        val statuses = results
            .map(MoveRuleResult::status)
            .toMutableSet()


        if (MoveRuleResult.Status.Impossible in statuses) {
            return Result(
                success = false,
                moveActions = mutableListOf()
            )
        } else if (MoveRuleResult.Status.BoxAheadNeedsToMove in statuses) {
            val nextPosition = position.nextInDirection(direction)

            if (state.tileAt(nextPosition)?.entity == null)
                throw IllegalStateException("Box ahead needs to move but there is no entity!")

            return determineMovesRecursively(state, nextPosition, direction).apply {
                if (success) {
                    moveActions += SimpleMoveAction(position, direction).incrementPushes()
                }
            }
        } else {
            return Result(
                success = true,
                moveActions = mutableListOf(
                    SimpleMoveAction(position, direction).incrementMoves()
                )
            )
        }
    }
}