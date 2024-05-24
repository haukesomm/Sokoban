package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.entityAt
import de.haukesomm.sokoban.core.moving.MoveRule
import de.haukesomm.sokoban.core.moving.MoveRuleResult

class MultipleBoxesPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> {
        val nextPosition = position.nextInDirection(direction)
        val secondNextPosition = position.nextInDirection(direction, steps = 2)

        val multipleBoxesAhead = listOf(nextPosition, secondNextPosition)
            .map(state::entityAt)
            .all { it?.isBox == true }

        return setOf(
            if (multipleBoxesAhead) MoveRuleResult.impossible("Multiple boxes ahead")
            else MoveRuleResult.possible()
        )
    }
}
