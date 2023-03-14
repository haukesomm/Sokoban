package de.haukesomm.sokoban.game.moving;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;

import java.util.*;

public class MoveCoordinator {

    private final Collection<MoveChecker> moveCheckers;

    public MoveCoordinator(MoveChecker... moveCheckers) {
        this.moveCheckers = Set.of(moveCheckers);
    }

    private static class Result {
        public final boolean success;
        public final List<MoveAction> moveActions;

        public Result(boolean success, List<MoveAction> moveActions) {
            this.success = success;
            this.moveActions = moveActions;
        }

        public static Result failure() {
            return new Result(false, new ArrayList<>());
        }
    }

    public GameState moveEntityIfPossible(GameState state, Entity entity, Direction direction) {
        var moveActionsResult = determineMoveActions(state, entity, direction);
        var resultingGameState = state;
        if (moveActionsResult.success) {
            for (var action : moveActionsResult.moveActions) {
                resultingGameState = action.performMove(resultingGameState);
            }
        }
        return resultingGameState;
    }

    private Result determineMoveActions(GameState state, Entity entity, Direction direction) {
        var moveCheckerResults = new HashSet<MoveCheckerResult>();
        moveCheckers.forEach(checker -> moveCheckerResults.add(checker.check(state, entity, direction)));

        if (moveCheckerResults.contains(MoveCheckerResult.IMPOSSIBLE)) {
            return Result.failure();
        } else if (moveCheckerResults.contains(MoveCheckerResult.ENTITY_AHEAD_NEEDS_TO_MOVE)) {
            var entityAhead = state.getEntityAtNextPositionOrNull(entity.position(), direction);
            var result = determineMoveActions(state, entityAhead, direction);
            if (result.success) {
                result.moveActions.add(new SimpleMoveAction(entity, direction));
            }
            return result;
        } else {
            var moveActions = new ArrayList<MoveAction>();
            moveActions.add(new SimpleMoveAction(entity, direction));
            return new Result(true, moveActions);
        }
    }
}
