package de.haukesomm.sokoban.core;

import de.haukesomm.sokoban.core.level.*;
import de.haukesomm.sokoban.core.moving.MoveCoordinator;
import de.haukesomm.sokoban.core.moving.MoveCoordinatorFactory;
import de.haukesomm.sokoban.core.moving.MoveCoordinatorResult;
import de.haukesomm.sokoban.core.moving.MoveValidatorStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameStateService {

    public interface StateChangeListener {
        void onGameStateChanged(GameState state, int moves, int pushes, boolean levelCleared);
    }


    private final LevelRepository levelRepository;

    private final LevelToGameStateConverter levelToGameStateConverter =
            new LevelToGameStateConverter(new SokobanLevelCharacterMap());

    private final MoveCoordinator moveCoordinator = MoveCoordinatorFactory.newWithDefaultValidators();

    private final Set<StateChangeListener> stateChangeListeners = new HashSet<>();


    public GameStateService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }


    private GameState state;

    private int moves = 0;

    private int pushes = 0;

    private boolean levelCleared = false;


    private void notifyGameStateChangedListeners() {
        stateChangeListeners.forEach(l -> l.onGameStateChanged(state, moves, pushes, levelCleared));
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
            throw new IllegalArgumentException("Level with id '" + levelId + "' does not exist!");
        }

        moves = 0;
        pushes = 0;
        levelCleared = false;

        state = levelToGameStateConverter.convert(level);
        notifyGameStateChangedListeners();
    }

    @Nullable
    public Entity getPlayer() {
        return state.getPlayer();
    }

    public void moveEntityIfPossible(Entity entity, Direction direction) {
        if (state == null) {
            throw new IllegalStateException("Cannot move entity: Game state has not been initialized!");
        }

        MoveCoordinatorResult coordinatorResult = moveCoordinator.moveEntityIfPossible(state, entity, direction);
        if (coordinatorResult.success()) {
            moves++;
            if (coordinatorResult.moveValidatorStatuses().contains(MoveValidatorStatus.BOX_AHEAD_NEEDS_TO_MOVE)) {
                pushes++;
            }
        }

        if (coordinatorResult.gameState().isPresent()) {
            var newGameState = coordinatorResult.gameState().get();
            levelCleared = checkLevelCleared(newGameState);
            state = newGameState;
        }

        notifyGameStateChangedListeners();
    }

    private boolean checkLevelCleared(GameState gameState) {
        for (int y = 0; y < gameState.getMapHeight(); y++) {
            for (int x = 0; x < gameState.getMapWidth(); x++) {
                var tile = gameState.tiles()[y][x];

                if (tile.type() != TileType.TARGET) {
                    continue;
                }

                var entityAtTarget = gameState.getEntityAtPositionOrNull(new Position(x, y));
                if (entityAtTarget == null || entityAtTarget.type() != EntityType.BOX) {
                    return false;
                }
            }
        }
        return true;
    }
}
