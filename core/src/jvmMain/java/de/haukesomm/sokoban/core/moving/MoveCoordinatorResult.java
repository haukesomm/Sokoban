package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.moving.validation.MoveValidatorStatus;

import java.util.Optional;
import java.util.Set;

public record MoveCoordinatorResult(
        boolean success,
        Set<MoveValidatorStatus> moveValidatorStatuses,
        Optional<GameState> gameState
) {
}
