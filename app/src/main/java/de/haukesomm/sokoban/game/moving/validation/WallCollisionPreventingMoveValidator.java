package de.haukesomm.sokoban.game.moving.validation;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.TileType;
import de.haukesomm.sokoban.game.moving.MoveValidatorStatus;

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
