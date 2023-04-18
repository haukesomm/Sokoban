package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.moving.validation.MoveValidatorStatus

data class MoveCoordinatorResult(
    val success: Boolean,
    val moveValidatorStatuses: Set<MoveValidatorStatus>,
    val gameState: GameState?
)
