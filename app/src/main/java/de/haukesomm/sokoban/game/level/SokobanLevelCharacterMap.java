package de.haukesomm.sokoban.game.level;

import de.haukesomm.sokoban.game.EntityType;
import de.haukesomm.sokoban.game.TileType;

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
