package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

/**
 * A special `MoveRule` that only checks a given collection of [moveRules] when the given
 * [condition] does not yield a status of [MoveRuleResult.Impossible].
 */
internal class ConditionalMoveRule(
    private val condition: MoveRule,
    private val moveRules: Collection<MoveRule>
) : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> =
        buildSet {
            addAll(condition.check(state, position, direction))
            if (none { it.status == MoveRuleResult.Status.Impossible })
                addAll(moveRules.checkAll(state, position, direction))
        }
}
