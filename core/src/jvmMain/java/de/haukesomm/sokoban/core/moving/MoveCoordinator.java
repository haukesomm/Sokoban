package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.moving.validation.MoveValidator;

import java.util.*;

public class MoveCoordinator {

    private final Collection<MoveValidator> moveValidators;

    public MoveCoordinator(MoveValidator... moveValidators) {
        this.moveValidators = Set.of(moveValidators);
    }

    private record Result(
            boolean success,
            List<MoveAction> moveActions,
            Set<MoveValidatorStatus> moveValidatorStatuses
    ) {
        public static Result failure(Set<MoveValidatorStatus> moveValidatorStatuses) {
            return new Result(false, new ArrayList<>(), moveValidatorStatuses);
        }
    }

    public MoveCoordinatorResult moveEntityIfPossible(GameState state, Entity entity, Direction direction) {
        var moveActionsResult = determineMoveActions(state, entity, direction);

        Optional<GameState> resultingGameStateOptional;
        if (moveActionsResult.success) {
            var tmpState = state;
            for (var action : moveActionsResult.moveActions) {
                tmpState = action.performMove(tmpState);
            }
            resultingGameStateOptional = Optional.of(tmpState);
        } else {
            resultingGameStateOptional = Optional.empty();
        }

        return new MoveCoordinatorResult(
                moveActionsResult.success,
                moveActionsResult.moveValidatorStatuses,
                resultingGameStateOptional
        );
    }

    private Result determineMoveActions(GameState state, Entity entity, Direction direction) {
        var moveValidatorStatuses = new HashSet<MoveValidatorStatus>();
        moveValidators.forEach(checker -> moveValidatorStatuses.addAll(checker.check(state, entity, direction)));

        if (moveValidatorStatuses.contains(MoveValidatorStatus.IMPOSSIBLE)) {
            return Result.failure(moveValidatorStatuses);
        } else if (moveValidatorStatuses.contains(MoveValidatorStatus.BOX_AHEAD_NEEDS_TO_MOVE)) {
            var entityAhead = state.getEntityAtNextPositionOrNull(entity.position(), direction);
            var result = determineMoveActions(state, entityAhead, direction);
            if (result.success) {
                result.moveValidatorStatuses.addAll(moveValidatorStatuses);
                result.moveActions.add(new SimpleMoveAction(entity, direction));
            }
            return result;
        } else {
            var moveActions = new ArrayList<MoveAction>();
            moveActions.add(new SimpleMoveAction(entity, direction));
            return new Result(true, moveActions, moveValidatorStatuses);
        }
    }
}
