package de.haukesomm.sokoban.game.moving.validation;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.moving.MoveValidatorStatus;

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
