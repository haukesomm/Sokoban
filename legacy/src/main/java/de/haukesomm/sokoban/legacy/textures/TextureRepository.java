package de.haukesomm.sokoban.legacy.textures;

import de.haukesomm.sokoban.core.model.Tile;

import javax.swing.*;

public interface TextureRepository {

    ImageIcon getForTileType(Tile tileType);
}
