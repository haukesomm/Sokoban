package de.haukesomm.sokoban.core.moving.validation;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.EntityType;
import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.moving.MoveValidatorStatus;

import java.util.Collection;

public class MultipleBoxesPreventingMoveValidator extends AbstractMoveValidator {

    @Override
    public Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction) {
        var entityAhead = state.getEntityAtNextPositionOrNull(entity.position(), direction);
        if (entityAhead != null && entityAhead.type() == EntityType.BOX) {
            var secondEntity = state.getEntityAtNextPositionOrNull(entityAhead.position(), direction);
            if (secondEntity != null && secondEntity.type() == EntityType.BOX) {
                return singleResult(MoveValidatorStatus.IMPOSSIBLE);
            }
        }
        return singleResult(MoveValidatorStatus.POSSIBLE);
    }
}
