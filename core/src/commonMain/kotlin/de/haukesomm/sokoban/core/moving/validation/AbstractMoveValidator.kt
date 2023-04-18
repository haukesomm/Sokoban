package de.haukesomm.sokoban.core.moving.validation

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

abstract class AbstractMoveValidator : MoveValidator {

    protected fun singleResult(result: MoveValidatorStatus): Collection<MoveValidatorStatus> =
        setOf(result)

    abstract override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveValidatorStatus>
}
