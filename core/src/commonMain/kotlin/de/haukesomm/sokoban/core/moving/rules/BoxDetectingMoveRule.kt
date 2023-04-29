package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

class BoxDetectingMoveRule : MoveRule {
    override val title: String = "Detect Boxes"
    override val description: String = "Detects boxes and lets the player push them"

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveRuleResult> {
        val entityAhead = state.nextEntityInDirection(entity.position, direction)

        return if (entityAhead?.isBox == true) setOf(MoveRuleResult.BoxAheadNeedsToMove)
        else setOf(MoveRuleResult.Possible)
    }
}
