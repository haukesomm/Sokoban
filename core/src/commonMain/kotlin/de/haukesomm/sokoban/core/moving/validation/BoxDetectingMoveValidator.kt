package de.haukesomm.sokoban.core.moving.validation

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.GameState

class BoxDetectingMoveValidator : AbstractMoveValidator() {

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveValidatorStatus> {
        val entityAhead = state.getEntityAtNextPositionOrNull(entity.position, direction)

        return if (entityAhead != null && entityAhead.type === EntityType.BOX) {
            singleResult(MoveValidatorStatus.BOX_AHEAD_NEEDS_TO_MOVE)
        } else {
            singleResult(MoveValidatorStatus.POSSIBLE)
        }
    }
}
