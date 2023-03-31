package de.haukesomm.sokoban.game.moving.validation;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.moving.MoveValidatorStatus;

import java.util.Collection;

public class PreconditionMoveValidator extends AbstractMoveValidator {
    private final MoveValidator preconditionMoveValidator;
    private final Collection<MoveValidator> moveValidators;

    public PreconditionMoveValidator(
            MoveValidator preconditionMoveValidator,
            Collection<MoveValidator> moveValidators
    ) {
        this.preconditionMoveValidator = preconditionMoveValidator;
        this.moveValidators = moveValidators;
    }

    @Override
    public Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction) {
        var statuses = preconditionMoveValidator.check(state, entity, direction);
        if (statuses.contains(MoveValidatorStatus.IMPOSSIBLE)) {
            return statuses;
        }

        moveValidators.stream()
                .map(validator -> validator.check(state, entity, direction))
                .forEach(statuses::addAll);

        return statuses;
    }
}