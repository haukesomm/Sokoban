package de.haukesomm.sokoban.core.moving.validation;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.moving.MoveValidatorStatus;

import java.util.Collection;

public interface MoveValidator {

    Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction);
}
