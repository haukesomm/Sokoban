package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.levelCleared
import de.haukesomm.sokoban.core.moving.MoveRule
import de.haukesomm.sokoban.core.moving.MoveRuleResult

class MoveAfterCompletionPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> =
        listOf(
            if (state.levelCleared) MoveRuleResult.impossible("Level has been cleared")
            else MoveRuleResult.possible()
        )
}