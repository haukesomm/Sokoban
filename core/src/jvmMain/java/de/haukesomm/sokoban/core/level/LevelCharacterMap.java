package de.haukesomm.sokoban.core.level;

import de.haukesomm.sokoban.core.*;

import java.util.Optional;

public interface LevelCharacterMap {

    TileType getTileType(char character);

    Optional<EntityType> getEntityType(char character);
}
