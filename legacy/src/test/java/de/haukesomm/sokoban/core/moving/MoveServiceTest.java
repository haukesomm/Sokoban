package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.*;
import de.haukesomm.sokoban.core.CharacterMaps;
import de.haukesomm.sokoban.core.level.Level;
import de.haukesomm.sokoban.core.level.LevelToGameStateConverter;
import de.haukesomm.sokoban.core.moving.rules.MultipleBoxesPreventingMoveRule;
import de.haukesomm.sokoban.core.moving.rules.WallCollisionPreventingMoveRule;
import de.haukesomm.sokoban.core.state.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MoveServiceTest {

    private final LevelToGameStateConverter converter = new LevelToGameStateConverter(CharacterMaps.getDefault());

    @SuppressWarnings("DataFlowIssue")
    private GameState newTestGameState() {
        return converter.convert(
                new Level(
                        "test-level",
                        "Test Level",
                        6,
                        5,
                        """
                                ######
                                #____#
                                #@$__#
                                #_$$_#
                                ######"""
                )
        );
    }

    private MoveService newMoveServiceWithBasicRules() {
        return MoveService.withDefaultRules(
                List.of(
                        new WallCollisionPreventingMoveRule(),
                        new MultipleBoxesPreventingMoveRule()
                )
        );
    }

    @Test
    @DisplayName("Given a non-checking coordinator, when the player Entity is moved, then it's position is updated")
    @SuppressWarnings("DataFlowIssue")
    public void noRulesWhenPlayerIsMovedHasUpdatedPosition() {
        var gameState = newTestGameState();
        var sut = newMoveServiceWithBasicRules();
        System.out.println(GameStateSerialization.toLayoutString(gameState));

        var playerPostion = gameState.getPlayerPosition();
        System.out.println(playerPostion);
        var result = sut.moveEntityIfPossible(gameState, playerPostion, Direction.Bottom);
        var plplayerPositionyer = result.getPlayerPosition();

        Assertions.assertEquals(new Position(1, 3), plplayerPositionyer);
    }

    @Test
    @DisplayName("Given a coordinator with default rules, when blocked by a wall Tile, then no follow up state is generated")
    @SuppressWarnings("DataFlowIssue")
    public void defaultRulesWhenBlockedByWallNothingIsMoved() {
        var gameState = newTestGameState();
        var sut = newMoveServiceWithBasicRules();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayerPosition(), Direction.Left);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("Given a coordinator with default rules, when a box blocks the move, then it is moved")
    @SuppressWarnings("DataFlowIssue")
    public void defaultRulesWhenBoxBlocksMoveItIsMovedToo() {
        var gameState = newTestGameState();
        var sut = newMoveServiceWithBasicRules();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayerPosition(), Direction.Right);

        Assertions.assertTrue(result.entityAt(new Position(2, 2)).isPlayer());
        Assertions.assertTrue(result.entityAt(new Position(3, 2)).isBox());
    }

    @Test
    @DisplayName("Given a coordinator with default rules, when there are two boxes in a row, then none are moved")
    @SuppressWarnings("DataFlowIssue")
    public void defaultCheckersWhenTwoBoxesAreInARowNoneAreMoved() {
        var gameState = newTestGameState();
        var sut = newMoveServiceWithBasicRules();

        var result = sut.moveEntityIfPossible(gameState, gameState.getPlayerPosition(), Direction.Bottom);
        result = sut.moveEntityIfPossible(result, result.getPlayerPosition(), Direction.Right);

        Assertions.assertNull(result);
    }
}
