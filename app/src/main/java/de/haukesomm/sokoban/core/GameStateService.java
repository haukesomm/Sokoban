package de.haukesomm.sokoban.core;

import de.haukesomm.sokoban.core.moving.MoveCoordinator;
import de.haukesomm.sokoban.core.moving.MoveCoordinatorFactory;

import java.util.HashSet;
import java.util.Set;

public class GameStateService {

    private final MoveCoordinator moveCoordinator = MoveCoordinatorFactory.newWithDefaultValidators();

    private GameState state;

    interface GameStateChangedListener {
        void onGameStateChanged(GameState state);
    }

    private final Set<GameStateChangedListener> gameStateChangedListeners = new HashSet<>();

    public GameStateService(GameState initialState) {
        this.state = initialState;
    }

    private void notifyGameStateChangedListeners() {
        gameStateChangedListeners.forEach(l -> l.onGameStateChanged(state));
    }

    public void addGameStateChangedListener(GameStateChangedListener listener) {
        gameStateChangedListeners.add(listener);
    }

    public void moveEntityIfPossible(Entity entity, Direction direction) {
        state = moveCoordinator.moveEntityIfPossible(state, entity, direction);
        notifyGameStateChangedListeners();
    }
}
