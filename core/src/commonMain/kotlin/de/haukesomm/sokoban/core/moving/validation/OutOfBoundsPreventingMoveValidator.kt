package de.haukesomm.sokoban.core.moving.validation

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.GameState

class OutOfBoundsPreventingMoveValidator : AbstractMoveValidator() {

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveValidatorStatus> {
        val (x, y) = entity.position.nextInDirection(direction)
        if (x < 0 || y < 0 || x >= state.getMapWidth() || y >= state.getMapHeight()) {
            // TODO: Use logger with warning level
            println("Warning: Level is not fully surrounded by walls!")
            return singleResult(MoveValidatorStatus.IMPOSSIBLE)
        }
        return singleResult(MoveValidatorStatus.POSSIBLE)
    }
}
