package de.haukesomm.sokoban.legacy.textures;

import de.haukesomm.sokoban.core.EntityType;
import de.haukesomm.sokoban.core.TileType;

import javax.swing.*;

public interface TextureRepository {

    ImageIcon getForTileType(TileType tileType);

    ImageIcon getForEntityType(EntityType entityType);
}
