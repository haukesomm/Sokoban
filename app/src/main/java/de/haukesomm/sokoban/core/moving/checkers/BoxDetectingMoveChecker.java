package de.haukesomm.sokoban.core.moving.checkers;

import de.haukesomm.sokoban.core.moving.MoveChecker;
import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.EntityType;
import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.moving.MoveCheckerResult;

public class BoxDetectingMoveChecker implements MoveChecker {

    @Override
    public MoveCheckerResult check(GameState state, Entity entity, Direction direction) {
        var entityAhead = state.getEntityAtNextPositionOrNull(entity.position(), direction);
        if (entityAhead != null && entityAhead.type() == EntityType.BOX) {
            return MoveCheckerResult.ENTITY_AHEAD_NEEDS_TO_MOVE;
        }
        return MoveCheckerResult.POSSIBLE;
    }
}
