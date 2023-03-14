package de.haukesomm.sokoban.game.moving.checkers;

import de.haukesomm.sokoban.game.moving.MoveChecker;
import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.TileType;
import de.haukesomm.sokoban.game.moving.MoveCheckerResult;

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
