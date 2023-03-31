package de.haukesomm.sokoban.game.moving.validation;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.moving.MoveValidatorStatus;

import java.util.Collection;

public class OutOfBoundsPreventingMoveValidator extends AbstractMoveValidator {

    @Override
    public Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction) {
        var nextPosition = entity.position().nextInDirection(direction);
        if (nextPosition.x() < 0 ||
                nextPosition.y() < 0 ||
                nextPosition.x() >= state.getMapWidth() ||
                nextPosition.y() >= state.getMapHeight()) {
            // TODO: Use logger with warning level
            System.err.println("Warning: Level is not fully surrounded by walls!");
            return singleResult(MoveValidatorStatus.IMPOSSIBLE);
        }

        return singleResult(MoveValidatorStatus.POSSIBLE);
    }
}
