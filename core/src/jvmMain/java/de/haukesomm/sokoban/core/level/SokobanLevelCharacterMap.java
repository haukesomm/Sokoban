package de.haukesomm.sokoban.core.level;

import de.haukesomm.sokoban.core.EntityType;
import de.haukesomm.sokoban.core.TileType;

import java.util.Optional;

public class SokobanLevelCharacterMap implements LevelCharacterMap {

    @Override
    public TileType getTileType(char character) {
        return switch (character) {
            case '#' -> TileType.WALL;
            case '.' -> TileType.TARGET;
            default -> TileType.NOTHING;
        };
    }

    @Override
    public Optional<EntityType> getEntityType(char character) {
        return switch (character) {
            case '@' -> Optional.of(EntityType.PLAYER);
            case '$' -> Optional.of(EntityType.BOX);
            default -> Optional.empty();
        };
    }
}
