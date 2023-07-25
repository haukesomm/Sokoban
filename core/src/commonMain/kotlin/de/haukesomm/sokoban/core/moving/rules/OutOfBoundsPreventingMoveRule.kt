package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

internal class OutOfBoundsPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> {
        val (x, y) = position.nextInDirection(direction)
        return setOf(
            if (x !in 0..state.width || y !in 0..state.height) MoveRuleResult.Impossible
            else MoveRuleResult.Possible
        )
    }
}
