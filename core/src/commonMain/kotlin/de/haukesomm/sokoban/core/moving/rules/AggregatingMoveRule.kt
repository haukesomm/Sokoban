package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

class AggregatingMoveRule(
    private vararg val rules: MoveRule
) : MoveRule {
    override val title: String = "Combine the results of multiple other rules"
    override val description: String = "Combine the results of multiple other rules"

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveRuleResult> =
        rules.flatMap { it.check(state, entity, direction) }
}