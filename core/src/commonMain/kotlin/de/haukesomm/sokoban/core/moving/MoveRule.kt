package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.Position

/**
 * A rule that determines whether an entity can be moved in a given direction.
 */
fun interface MoveRule {

    /**
     * Checks whether an entity can be moved in a given direction.
     *
     * The method returns a collection of [MoveRuleResult]s. If the collection is empty, the entity can be moved.
     * Otherwise, the entity cannot be moved or is blocked by another entity. Both states are represented by
     * the respective [MoveRuleResult] values.
     */
    fun check(
        state: GameState,
        position: Position,
        direction: Direction
    ): Collection<MoveRuleResult>
}

/**
 * Convenience function to check a collection of [MoveRule]s.
 */
fun Collection<MoveRule>.checkAll(
    state: GameState,
    position: Position,
    direction: Direction
): Collection<MoveRuleResult> =
    flatMap { it.check(state, position, direction) }
