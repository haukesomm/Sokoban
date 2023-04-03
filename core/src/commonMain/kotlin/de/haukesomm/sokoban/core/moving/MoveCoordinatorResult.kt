package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.moving.validation.MoveValidatorStatus
import kotlin.jvm.JvmRecord

@JvmRecord
data class MoveCoordinatorResult(
    val success: Boolean,
    val moveValidatorStatuses: Set<MoveValidatorStatus>,
    val gameState: GameState?
)
