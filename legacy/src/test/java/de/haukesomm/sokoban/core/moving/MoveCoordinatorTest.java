package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.*;
import de.haukesomm.sokoban.core.level.LevelRepository;
import de.haukesomm.sokoban.core.level.LevelToGameStateConverter;
import de.haukesomm.sokoban.core.level.SokobanLevelCharacterMap;
import de.haukesomm.sokoban.core.state.*;
import de.haukesomm.sokoban.legacy.level.JarResourceLevelRepository;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class MoveCoordinatorTest {
    private final LevelToGameStateConverter converter = new LevelToGameStateConverter(
            new SokobanLevelCharacterMap()
    );
    private final LevelRepository repository = new JarResourceLevelRepository(6, 5);

    private GameState newTestGameState() {
        return converter.convert(
                repository.getLevelOrNull(
                        repository.getAvailableLevels().get(0).getId()
                )
        );
    }

    @Test
    @DisplayName("Given a non-checking coordinator, when the player Entity is moved, then it's position is updated")
    @SuppressWarnings("DataFlowIssue")
    public void noCheckersWhenPlayerIsMovedHasUpdatedPosition() {
        var gameState = newTestGameState();
        var sut = new MoveCoordinator();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.BOTTOM);
        var player = result.getGameState().getPlayer();

        Assertions.assertEquals(new Position(1, 3), player.getPosition());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when blocked by a wall Tile, then no follow up state is generated")
    @SuppressWarnings("DataFlowIssue")
    public void defaultCheckersWhenBlockedByWallNothingIsMoved() {
        var gameState = newTestGameState();
        var sut = MoveCoordinatorFactory.newWithDefaultValidators();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.LEFT);

        Assertions.assertNull(result.getGameState());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when a box blocks the move, then it is moved")
    @SuppressWarnings("DataFlowIssue")
    public void defaultCheckersWhenBoxBlocksMoveItIsMovedToo() {
        var gameState = newTestGameState();
        var sut = MoveCoordinatorFactory.newWithDefaultValidators();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.RIGHT);
        var state = result.getGameState();

        Assertions.assertTrue(state.entityAt(2, 2).isPlayer());
        Assertions.assertTrue(state.entityAt(3, 2).isBox());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when there are two boxes in a row, then none are moved")
    @SuppressWarnings("DataFlowIssue")
    public void defaultCheckersWhenTwoBoxesAreInARowNoneAreMoved() {
        var gameState = newTestGameState();
        var sut = MoveCoordinatorFactory.newWithDefaultValidators();

        var result = sut
                .moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.BOTTOM)
                .getGameState();
        result = sut
                .moveEntityIfPossible(result, result.getPlayer(), Direction.RIGHT)
                .getGameState();

        Assertions.assertNull(result);
    }
}
