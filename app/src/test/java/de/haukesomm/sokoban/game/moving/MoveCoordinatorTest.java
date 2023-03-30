package de.haukesomm.sokoban.game.moving;

import de.haukesomm.sokoban.game.*;
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
                new Tile[][]{
                        { new Tile(TileType.WALL), new Tile(TileType.WALL),     new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.NOTHING),  new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.NOTHING),  new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.NOTHING),  new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.NOTHING), new Tile(TileType.WALL) },
                        { new Tile(TileType.WALL), new Tile(TileType.WALL),     new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL),    new Tile(TileType.WALL) },
                },
                Set.of(
                        new Entity("player", EntityType.PLAYER, new Position(1, 2)),
                        new Entity("box-0", EntityType.BOX, new Position(2, 2)),
                        new Entity("box-1", EntityType.BOX, new Position(2, 3)),
                        new Entity("box-2", EntityType.BOX, new Position(3, 3))
                )

        );
    }

    @Test
    @DisplayName("Given a non-checking coordinator, when the player Entity is moved, then it's position is updated")
    public void noCheckersWhenPlayerIsMovedHasUpdatedPosition() {
        var gameState = newTestGameState();
        var sut = new MoveCoordinator();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer().orElseThrow(), Direction.BOTTOM);

        Assertions.assertEquals(new Position(1, 3), result.getPlayer().orElseThrow().position());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when blocked by a wall Tile, then nothing is moved")
    public void defaultCheckersWhenBlockedByWallNothingIsMoved() {
        var gameState = newTestGameState();
        var sut = MoveCoordinatorFactory.newWithDefaultValidators();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer().orElseThrow(), Direction.LEFT);

        Assertions.assertEquals(new Position(1, 2), result.getPlayer().orElseThrow().position());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when a box blocks the move, then it is moved")
    public void defaultCheckersWhenBoxBlocksMoveItIsMovedToo() {
        var gameState = newTestGameState();
        var sut = MoveCoordinatorFactory.newWithDefaultValidators();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer().orElseThrow(), Direction.RIGHT);

        Assertions.assertEquals(new Position(2, 2), result.getPlayer().orElseThrow().position());
        Assertions.assertEquals(new Position(3, 2), result.getEntityById("box-0").position());
    }

    @Test
    @DisplayName("Given a coordinator with default checkers, when there are two boxes in a row, then none are moved")
    public void defaultCheckersWhenTwoBoxesAreInARowNoneAreMoved() {
        var gameState = newTestGameState();
        var sut = MoveCoordinatorFactory.newWithDefaultValidators();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayer().orElseThrow(), Direction.BOTTOM);
        result = sut.moveEntityIfPossible(result, gameState.getPlayer().orElseThrow(), Direction.RIGHT);

        Assertions.assertEquals(new Position(1, 3), result.getPlayer().orElseThrow().position());
        Assertions.assertEquals(new Position(2, 3), result.getEntityById("box-1").position());
        Assertions.assertEquals(new Position(3, 3), result.getEntityById("box-2").position());
    }
}
