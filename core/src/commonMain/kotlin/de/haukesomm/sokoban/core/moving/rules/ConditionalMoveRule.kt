package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

/**
 * A special `MoveRule` that only checks a given collection of [moveRules] when the given
 * [condition] does not yield a status of [MoveRuleResult.Impossible].
 */
internal class ConditionalMoveRule(
    private val condition: MoveRule,
    private val moveRules: Collection<MoveRule>
) : MoveRule {
    override val title: String = "[Internal] Execute a set of rules conditionally"
    override val description: String = "Execute a set of rules when the conditional rule succeeds"

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveRuleResult> {
        val statuses = condition.check(state, entity, direction)

        return if (statuses.contains(MoveRuleResult.Impossible)) statuses
        else statuses + moveRules.checkAll(state, entity, direction)
    }
}
