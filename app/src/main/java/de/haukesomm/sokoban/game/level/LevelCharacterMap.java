package de.haukesomm.sokoban.game.level;

import de.haukesomm.sokoban.game.*;

import java.util.Optional;

public interface LevelCharacterMap {

    TileType getTileType(char character);

    Optional<EntityType> getEntityType(char character);
}
