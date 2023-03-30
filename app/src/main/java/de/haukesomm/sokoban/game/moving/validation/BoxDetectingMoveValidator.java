package de.haukesomm.sokoban.game.moving.validation;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.EntityType;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.moving.MoveValidatorStatus;

import java.util.Collection;

public class BoxDetectingMoveValidator extends AbstractMoveValidator {

    @Override
    public Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction) {
        var entityAhead = state.getEntityAtNextPositionOrNull(entity.position(), direction);
        if (entityAhead != null && entityAhead.type() == EntityType.BOX) {
            return singleResult(MoveValidatorStatus.BOX_AHEAD_NEEDS_TO_MOVE);
        }
        return singleResult(MoveValidatorStatus.POSSIBLE);
    }
}
