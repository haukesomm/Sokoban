package de.haukesomm.sokoban.legacy.textures;

import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.Tile;
import javax.swing.*;

public interface TextureRepository {

    ImageIcon getForTileType(Tile.Type tileType);

    ImageIcon getForEntityType(Entity.Type entityType);
}
