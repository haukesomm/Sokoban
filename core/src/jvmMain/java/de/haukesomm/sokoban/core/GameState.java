package de.haukesomm.sokoban.core;

import java.util.Collection;
import java.util.Optional;

public record GameState(Tile[][] tiles, Collection<Entity> entities) {

    public int getMapWidth() {
        return tiles[0].length;
    }

    public int getMapHeight() {
        return tiles.length;
    }

    public Tile getNextTileInDirectionOrNull(Position position, Direction direction) {
        var nextPosition = position.nextInDirection(direction);
        if (nextPosition.y() > tiles.length ||
                (tiles.length > 0 && nextPosition.x() > tiles[0].length)
        ) {
            return null;
        }
        return tiles[nextPosition.y()][nextPosition.x()];
    }

    public Entity getEntityAtPositionOrNull(Position position) {
        for (var entity : entities) {
            if (entity.position().equals(position)) {
                return entity;
            }
        }
        return null;
    }

    public Entity getEntityAtNextPositionOrNull(Position position, Direction direction) {
        return getEntityAtPositionOrNull(position.nextInDirection(direction));
    }

    public Optional<Entity> getPlayer() {
        return entities
                .stream()
                .filter(e -> e.type() == EntityType.PLAYER)
                .findFirst();
    }

    public Entity getEntityById(String id) {
        return entities
                .stream()
                .filter(e -> e.id().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
