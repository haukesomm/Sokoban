package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

class WallCollisionPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> {
        val nextTile = state.tileInDirection(position, direction)

        return setOf(
            if (nextTile?.isWall == true) MoveRuleResult.impossible("Wall ahead")
            else MoveRuleResult.possible()
        )
    }
}
