package de.haukesomm.sokoban.game.moving.validation;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.EntityType;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.moving.MoveValidatorStatus;

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
