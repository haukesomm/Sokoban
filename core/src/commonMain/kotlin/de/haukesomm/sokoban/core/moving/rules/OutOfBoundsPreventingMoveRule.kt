package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.moving.MoveRule
import de.haukesomm.sokoban.core.moving.MoveRuleResult

internal class OutOfBoundsPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> {
        val (x, y) = position.nextInDirection(direction)
        return setOf(
            if (x !in 0 until state.width ||
                y !in 0 until state.height) {
                MoveRuleResult.impossible("Out of bounds")
            } else {
                MoveRuleResult.possible()
            }
        )
    }
}
