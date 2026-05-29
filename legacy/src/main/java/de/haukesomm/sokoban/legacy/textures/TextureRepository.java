package de.haukesomm.sokoban.legacy.textures;

import de.haukesomm.sokoban.core.Tile;

import javax.swing.*;

public interface TextureRepository {

    ImageIcon getForTileType(Tile tileType);
}
