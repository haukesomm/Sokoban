package de.haukesomm.sokoban.game.moving;

import de.haukesomm.sokoban.game.GameState;

import java.util.Optional;
import java.util.Set;

public record MoveCoordinatorResult(
        boolean success,
        Set<MoveValidatorStatus> moveValidatorStatuses,
        Optional<GameState> gameState
) {
}
