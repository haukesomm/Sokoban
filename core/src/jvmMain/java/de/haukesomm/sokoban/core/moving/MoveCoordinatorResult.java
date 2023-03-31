package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.GameState;

import java.util.Optional;
import java.util.Set;

public record MoveCoordinatorResult(
        boolean success,
        Set<MoveValidatorStatus> moveValidatorStatuses,
        Optional<GameState> gameState
) {
}
