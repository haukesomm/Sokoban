package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameState;

public interface MoveChecker {

    MoveCheckerResult check(GameState state, Entity entity, Direction direction);
}
