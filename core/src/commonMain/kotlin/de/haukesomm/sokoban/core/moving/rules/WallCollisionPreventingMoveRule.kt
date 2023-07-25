package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

class WallCollisionPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> {
        val nextTile = state.tileInDirection(position, direction)

        return if (nextTile?.isWall == true) setOf(MoveRuleResult.Impossible)
        else setOf(MoveRuleResult.Possible)
    }
}
