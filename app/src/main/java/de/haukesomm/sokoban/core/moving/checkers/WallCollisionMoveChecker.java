package de.haukesomm.sokoban.core.moving.checkers;

import de.haukesomm.sokoban.core.moving.MoveChecker;
import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.TileType;
import de.haukesomm.sokoban.core.moving.MoveCheckerResult;

public class WallCollisionMoveChecker implements MoveChecker {

    @Override
    public MoveCheckerResult check(GameState state, Entity entity, Direction direction) {
        var nextTile = state.getNextTileInDirectionOrNull(entity.position(), direction);
        if (nextTile != null && nextTile.type() == TileType.WALL) {
            return MoveCheckerResult.IMPOSSIBLE;
        }
        return MoveCheckerResult.POSSIBLE;
    }
}
