package de.haukesomm.sokoban.gui.textures;

import de.haukesomm.sokoban.game.EntityType;
import de.haukesomm.sokoban.game.TileType;

import javax.swing.*;

public interface TextureRepository {

    ImageIcon getForTileType(TileType tileType);

    ImageIcon getForEntityType(EntityType entityType);
}
