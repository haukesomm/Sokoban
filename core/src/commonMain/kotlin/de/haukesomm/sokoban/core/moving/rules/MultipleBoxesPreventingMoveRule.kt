package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

class MultipleBoxesPreventingMoveRule : MoveRule {

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveRuleResult> {
        val entityAhead = state.nextEntityInDirection(entity.position, direction)
        if (entityAhead?.isBox == true) {
            val secondEntity = state.nextEntityInDirection(entityAhead.position, direction)
            if (secondEntity?.isBox == true) {
                return setOf(MoveRuleResult.Impossible)
            }
        }
        return setOf(MoveRuleResult.Possible)
    }
}
