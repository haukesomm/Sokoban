package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

class MoveAfterCompletionPreventingMoveRule : MoveRule {
    override val title: String = "No moving after completion"
    override val description: String = "Prevents entities from moving once the level is cleared"

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveRuleResult> =
        listOf(
            if (state.levelCleared) MoveRuleResult.Impossible
            else MoveRuleResult.Possible
        )
}