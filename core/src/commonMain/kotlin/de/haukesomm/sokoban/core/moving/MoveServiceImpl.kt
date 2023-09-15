package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.*

/**
 * Implementation of [MoveService] which uses a set of [MoveRule]s to determine whether a move is possible.
 *
 * These rules are checked in order and the first rule which returns [MoveRuleResult.Status.Impossible] stops the
 * process and the method returns `null`. If the first rule returns [MoveRuleResult.Status.BoxAheadNeedsToMove], the
 * service tries to move the box ahead recursively. If this recursive call returns `null`, the method returns `null` as
 * well. Otherwise, the method returns a new [GameState] with the entity moved and the box moved as well.
 */
class MoveServiceImpl(private vararg val moveRules: MoveRule) : MoveService {

    private data class Result(
        val success: Boolean,
        val moveActions: MutableList<MoveAction>
    )

    override fun moveEntityIfPossible(state: GameState, position: Position, direction: Direction): GameState? {
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