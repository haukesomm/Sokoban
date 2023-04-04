package de.haukesomm.sokoban.core.moving.validation

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.GameState

class MultipleBoxesPreventingMoveValidator : AbstractMoveValidator() {

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveValidatorStatus> {
        val entityAhead = state.getEntityAtNextPositionOrNull(entity.position, direction)
        if (entityAhead != null && entityAhead.type === EntityType.BOX) {
            val secondEntity = state.getEntityAtNextPositionOrNull(entityAhead.position, direction)
            if (secondEntity != null && secondEntity.type === EntityType.BOX) {
                return singleResult(MoveValidatorStatus.IMPOSSIBLE)
            }
        }
        return singleResult(MoveValidatorStatus.POSSIBLE)
    }
}
