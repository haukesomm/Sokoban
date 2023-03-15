package de.haukesomm.sokoban.game;

import de.haukesomm.sokoban.game.level.*;
import de.haukesomm.sokoban.game.moving.MoveCoordinator;
import de.haukesomm.sokoban.game.moving.MoveCoordinatorFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameStateService {

    public interface StateChangeListener {
        void onGameStateChanged(GameState state);
    }


    private final LevelRepository levelRepository = new BuiltinLevelRepository();

    private final LevelToGameStateConverter levelToGameStateConverter =
            new LevelToGameStateConverter(new SokobanLevelCharacterMap());

    private final MoveCoordinator moveCoordinator = MoveCoordinatorFactory.newWithDefaultValidators();

    private final Set<StateChangeListener> stateChangeListeners = new HashSet<>();


    private GameState state;


    private void notifyGameStateChangedListeners() {
        stateChangeListeners.forEach(l -> l.onGameStateChanged(state));
    }

    public void addGameStateChangedListener(StateChangeListener listener) {
        stateChangeListeners.add(listener);
    }

    public List<LevelDescription> getAvailableLevels() {
        return levelRepository.getAvailableLevels();
    }

    public void loadLevel(String levelId) {
        var level = levelRepository.getLevelOrNull(levelId);
        if (level == null) {
            throw new IllegalArgumentException("Level with id '" + levelId +"' does not exist!");
        }
        state = levelToGameStateConverter.convert(level);
        notifyGameStateChangedListeners();
    }

    public Optional<Entity> getPlayer() {
        return state.getPlayer();
    }

    public void moveEntityIfPossible(Entity entity, Direction direction) {
        if (state == null) {
            throw new IllegalStateException("Cannot move entity: Game state has not been initialized!");
        }
        state = moveCoordinator.moveEntityIfPossible(state, entity, direction);
        notifyGameStateChangedListeners();
    }
}
