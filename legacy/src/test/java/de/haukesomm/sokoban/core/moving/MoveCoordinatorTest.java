package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class MoveCoordinatorTest {

    private GameState newTestGameState() {
        // ######
        // #____#
        // #@$__#
        // #_$$_#
        // ######
        return new GameState(
                "test-level",
                new Tile[][]{
                        { new Tile(TileType.WALL), new Tile(TileType.WALL),     new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.NOTHING),  new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.NOTHING),  new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.NOTHING),  new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.WALL),     new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL) },
                },
                Set.of(
                        new Entity("player", EntityType.PLAYER, new Position(1, 2), Direction.TOP),
                        new Entity("box-0", EntityType.BOX, new Position(2, 2), Direction.TOP),
                        new Entity("box-1", EntityType.BOX, new Position(2, 3), Direction.TOP),
                        new Entity("box-2", EntityType.BOX, new Position(3, 3), Direction.TOP)
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

        Assertions.assertEquals(new Position(2, 2), state.getPlayer().getPosition());
        Assertions.assertEquals(new Position(3, 2), state.getEntityById("box-0").getPosition());
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
                .moveEntityIfPossible(result, gameState.getPlayer(), Direction.RIGHT)
                .getGameState();

        Assertions.assertEquals(new Position(1, 3), result.getPlayer().getPosition());
        Assertions.assertEquals(new Position(2, 3), result.getEntityById("box-1").getPosition());
        Assertions.assertEquals(new Position(3, 3), result.getEntityById("box-2").getPosition());
    }
}
