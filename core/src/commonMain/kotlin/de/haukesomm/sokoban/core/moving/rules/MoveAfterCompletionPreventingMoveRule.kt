package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

class MoveAfterCompletionPreventingMoveRule : MoveRule {

    override fun check(state: GameState, position: Position, direction: Direction): Collection<MoveRuleResult> =
        listOf(
            if (state.levelCleared) MoveRuleResult.Impossible
            else MoveRuleResult.Possible
        )
}