package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

class BoxDetectingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> =
        setOf(
            if (state.entityInDirection(position, direction)?.isBox == true) MoveRuleResult.boxAheadNeedsToMove()
            else MoveRuleResult.possible()
        )
}
