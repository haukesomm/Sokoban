package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.moving.MoveRule
import de.haukesomm.sokoban.core.moving.MoveRuleResult
import de.haukesomm.sokoban.core.moving.checkAll

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