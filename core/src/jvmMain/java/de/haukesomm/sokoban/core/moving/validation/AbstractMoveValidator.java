package de.haukesomm.sokoban.core.moving.validation;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.moving.MoveValidatorStatus;

import java.util.Collection;
import java.util.HashSet;

public abstract class AbstractMoveValidator implements MoveValidator {

    protected Collection<MoveValidatorStatus> singleResult(MoveValidatorStatus result) {
        var resultSet = new HashSet<MoveValidatorStatus>();
        resultSet.add(result);
        return resultSet;
    }

    @Override
    public abstract Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction);
}
