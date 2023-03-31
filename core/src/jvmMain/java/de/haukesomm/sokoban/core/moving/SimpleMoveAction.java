package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameState;

import java.util.ArrayList;

public class SimpleMoveAction implements MoveAction {

    private final Entity entity;
    private final Direction direction;

    public SimpleMoveAction(Entity entity, Direction direction) {
        this.entity = entity;
        this.direction = direction;
    }

    @Override
    public GameState performMove(GameState state) {
        var entities = new ArrayList<>(state.entities());
        entities.remove(entity);

        var movedEntity = new Entity(entity.id(), entity.type(), entity.position().nextInDirection(direction));
        entities.add(movedEntity);

        return new GameState(state.tiles(), entities);
    }
}
