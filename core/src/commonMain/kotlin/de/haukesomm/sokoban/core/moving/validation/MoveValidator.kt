package de.haukesomm.sokoban.core.moving.validation

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.GameState

interface MoveValidator {

    fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveValidatorStatus>
}
