package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

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
 * Special [MoveRule] that has a name and a description, so it can be presented to the user.
 */
interface UserSelectableMoveRule : MoveRule {

    /**
     * The name of the rule.
     */
    val name: String

    /**
     * A more detailed description of the rule.
     */
    val description: String
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
