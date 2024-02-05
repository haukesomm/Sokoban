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

    override fun moveEntityIfPossible(state: GameState, position: Position, direction: Direction): GameState? {
        val result = determineMovesRecursively(state, direction, position, emptyList())

        return result?.fold(state) { acc, action ->
            action.performMove(acc)
        }?.transform { previous = state }
    }

    private tailrec fun determineMovesRecursively(
        state: GameState,
        direction: Direction,
        position: Position,
        acc: List<MoveAction>
    ): List<MoveAction>? {
        val entity = state.entityAt(position)
            ?: return run {
                println("Warning: Attempting to move an Entity that does not exist!")
                null
            }

        val status = moveRules
            .checkAll(state, position, direction)
            .maxBy(MoveRuleResult::status)
            .status

        val newAccWithMoveAction = listOf(SimpleMoveAction(position, direction).let { action ->
            if (entity.isBox) action.incrementPushes()
            else action.incrementMoves()
        }) + acc

        return when(status) {
            MoveRuleResult.Status.Impossible -> null

            MoveRuleResult.Status.BoxAheadNeedsToMove -> determineMovesRecursively(
                state,
                direction,
                position.nextInDirection(direction),
                newAccWithMoveAction
            )

            else -> newAccWithMoveAction
        }
    }
}