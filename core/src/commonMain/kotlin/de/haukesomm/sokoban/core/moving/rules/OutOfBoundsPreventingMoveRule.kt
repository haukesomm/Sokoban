package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

internal class OutOfBoundsPreventingMoveRule : MoveRule {
    override val title: String = "Don't move out of bounds"
    override val description: String = "Prevent moves outside the game field"

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveRuleResult> {
        val (x, y) = entity.position.nextInDirection(direction)
        return if (x < 0 || y < 0 || x >= state.width || y >= state.height) {
            setOf(MoveRuleResult.Impossible)
        } else {
            setOf(MoveRuleResult.Possible)
        }
    }
}
