package de.haukesomm.sokoban.game.moving.validation;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.moving.MoveValidatorStatus;

import java.util.Collection;

public interface MoveValidator {

    Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction);
}
