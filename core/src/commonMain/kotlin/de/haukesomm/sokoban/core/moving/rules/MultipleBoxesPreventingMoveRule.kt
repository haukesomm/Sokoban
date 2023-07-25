package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

class MultipleBoxesPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> {
        val nextPosition = position.nextInDirection(direction)
        val secondNextPosition = position.nextInDirection(direction, steps = 2)

        val multipleBoxesAhead = listOf(nextPosition, secondNextPosition)
            .map(state::entityAt)
            .all { it?.isBox == true }

        return setOf(
            if (multipleBoxesAhead) MoveRuleResult.Impossible
            else MoveRuleResult.Possible
        )
    }
}
