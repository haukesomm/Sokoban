package de.haukesomm.sokoban.core.moving.validation;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.TileType;
import de.haukesomm.sokoban.core.moving.MoveValidatorStatus;

import java.util.Collection;

public class WallCollisionPreventingMoveValidator extends AbstractMoveValidator {

    @Override
    public Collection<MoveValidatorStatus> check(GameState state, Entity entity, Direction direction) {
        var nextTile = state.getNextTileInDirectionOrNull(entity.position(), direction);
        if (nextTile != null && nextTile.type() == TileType.WALL) {
            return singleResult(MoveValidatorStatus.IMPOSSIBLE);
        }
        return singleResult(MoveValidatorStatus.POSSIBLE);
    }
}
