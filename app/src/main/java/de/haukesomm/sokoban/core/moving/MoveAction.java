package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.GameState;

public interface MoveAction {

    GameState performMove(GameState state);
}
