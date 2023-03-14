package de.haukesomm.sokoban.core;

import java.util.Collection;

public record GameState(Tile[][] tiles, Collection<Entity> entities) {

    public Tile getNextTileInDirectionOrNull(Position position, Direction direction) {
        var nextPosition = position.nextInDirection(direction);
        if (nextPosition.y() > tiles.length ||
                (tiles.length > 0 && nextPosition.x() > tiles[0].length)
        ) {
            return null;
        }
        return tiles[nextPosition.y()][nextPosition.x()];
    }

    public Entity getEntityAtNextPositionOrNull(Position position, Direction direction) {
        for (var entity : entities) {
            if (entity.position().equals(position.nextInDirection(direction))) {
                return entity;
            }
        }
        return null;
    }

    public Entity getPlayer() {
        return entities
                .stream()
                .filter(e -> e.type() == EntityType.PLAYER)
                .findFirst()
                .orElseThrow();
    }

    public Entity getEntityById(String id) {
        return entities
                .stream()
                .filter(e -> e.id().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
