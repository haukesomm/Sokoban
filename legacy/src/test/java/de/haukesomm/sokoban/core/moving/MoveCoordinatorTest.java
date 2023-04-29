package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.*;
import de.haukesomm.sokoban.core.level.DefaultTileFactory;
import de.haukesomm.sokoban.core.level.LevelRepository;
import de.haukesomm.sokoban.core.level.LevelToGameStateConverter;
import de.haukesomm.sokoban.core.state.*;
import de.haukesomm.sokoban.legacy.level.JarResourceLevelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MoveCoordinatorTest {
    private final LevelToGameStateConverter converter = new LevelToGameStateConverter(new DefaultTileFactory());
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

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.Bottom);
        var player = result.getPlayer();

        Assertions.assertEquals(new Position(1, 3), player.getPosition());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when blocked by a wall Tile, then no follow up state is generated")
    @SuppressWarnings("DataFlowIssue")
    public void defaultCheckersWhenBlockedByWallNothingIsMoved() {
        var gameState = newTestGameState();
        var sut = MoveCoordinator.withDefaultRules();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.Left);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when a box blocks the move, then it is moved")
    @SuppressWarnings("DataFlowIssue")
    public void defaultCheckersWhenBoxBlocksMoveItIsMovedToo() {
        var gameState = newTestGameState();
        var sut = MoveCoordinator.withDefaultRules();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.Right);

        Assertions.assertTrue(result.entityAt(new Position(2, 2)).isPlayer());
        Assertions.assertTrue(result.entityAt(new Position(3, 2)).isBox());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when there are two boxes in a row, then none are moved")
    @SuppressWarnings("DataFlowIssue")
    public void defaultCheckersWhenTwoBoxesAreInARowNoneAreMoved() {
        var gameState = newTestGameState();
        var sut = MoveCoordinator.withDefaultRules();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer(), Direction.Bottom);
        result = sut.moveEntityIfPossible(result, result.getPlayer(), Direction.Right);

        Assertions.assertNull(result);
    }
}
