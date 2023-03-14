package de.haukesomm.sokoban.game.moving.checkers;

import de.haukesomm.sokoban.game.moving.MoveChecker;
import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.EntityType;
import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.moving.MoveCheckerResult;

public class MultipleBoxesPreventingMoveChecker implements MoveChecker {

    @Override
    public MoveCheckerResult check(GameState state, Entity entity, Direction direction) {
        var entityAhead = state.getEntityAtNextPositionOrNull(entity.position(), direction);
        if (entityAhead != null && entityAhead.type() == EntityType.BOX) {
            var secondEntity = state.getEntityAtNextPositionOrNull(entityAhead.position(), direction);
            if (secondEntity != null && secondEntity.type() == EntityType.BOX) {
                return MoveCheckerResult.IMPOSSIBLE;
            }
        }
        return MoveCheckerResult.POSSIBLE;
    }
}
