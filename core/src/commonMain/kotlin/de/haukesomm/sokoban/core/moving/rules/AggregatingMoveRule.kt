package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

class AggregatingMoveRule(
    private vararg val rules: MoveRule
) : MoveRule {

    override fun check(
        state: GameState,
        position: Position,
        direction: Direction
    ): Collection<MoveRuleResult> =
        rules.toSet().checkAll(state, position, direction)
}