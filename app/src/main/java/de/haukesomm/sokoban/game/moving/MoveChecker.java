package de.haukesomm.sokoban.game.moving;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;

public interface MoveChecker {

    MoveCheckerResult check(GameState state, Entity entity, Direction direction);
}
