package de.haukesomm.sokoban.game.moving;

import de.haukesomm.sokoban.game.GameState;

public interface MoveAction {

    GameState performMove(GameState state);
}
