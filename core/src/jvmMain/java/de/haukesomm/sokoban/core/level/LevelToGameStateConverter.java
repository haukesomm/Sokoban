package de.haukesomm.sokoban.core.level;

import de.haukesomm.sokoban.core.*;

import java.util.HashSet;
import java.util.Optional;

public class LevelToGameStateConverter {

    private final LevelCharacterMap levelCharacterMap;

    public LevelToGameStateConverter(LevelCharacterMap levelCharacterMap) {
        this.levelCharacterMap = levelCharacterMap;
    }

    public GameState convert(Level level) {
        var tiles = new Tile[level.height][level.width];
        var entities = new HashSet<Entity>();

        var layoutString = level.layoutString;
        for (int i = 0; i < layoutString.length(); i++) {
            var character = layoutString.charAt(i);

            var tile = new Tile(levelCharacterMap.getTileType(character));
            var tilePosition = Position.fromIndex(i, level.width);
            tiles[tilePosition.y()][tilePosition.x()] = tile;

            var entityType = levelCharacterMap.getEntityType(character);
            if (entityType != null) {
                var position = Position.fromIndex(i, level.width);
                var entity = new Entity(Id.next(), entityType, position);
                entities.add(entity);
            }
        }

        return new GameState(tiles, entities);
    }
}
