package de.haukesomm.sokoban.core.moving.validation

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

class PreconditionMoveValidator(
    private val preconditionMoveValidator: MoveValidator,
    private val moveValidators: Collection<MoveValidator>
) : AbstractMoveValidator() {

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveValidatorStatus> {
        val statuses = preconditionMoveValidator.check(state, entity, direction).toMutableSet()
        if (statuses.contains(MoveValidatorStatus.IMPOSSIBLE)) {
            return statuses
        }

        moveValidators
            .map { it.check(state, entity, direction) }
            .forEach { statuses += it }

        return statuses
    }
}
