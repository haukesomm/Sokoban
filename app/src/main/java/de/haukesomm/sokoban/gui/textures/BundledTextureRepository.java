package de.haukesomm.sokoban.gui.textures;

import de.haukesomm.sokoban.game.EntityType;
import de.haukesomm.sokoban.game.TileType;

import javax.swing.*;

public class BundledTextureRepository implements TextureRepository {

    private static final String BASE_PATH = "/de/haukesomm/sokoban/textures";

    @Override
    public ImageIcon getForTileType(TileType tileType) {
        String textureFileName = switch (tileType) {
            case NOTHING -> "ground.png";
            case WALL -> "wall.png";
            case TARGET -> "target.png";
        };
        var resource = getClass().getResource(BASE_PATH + "/" + textureFileName);
        return new ImageIcon(resource);
    }

    @Override
    public ImageIcon getForEntityType(EntityType entityType) {
        String textureFileName = switch (entityType) {
            case BOX -> "box.png";
            case PLAYER -> "player0.png";
        };
        var resource = getClass().getResource(BASE_PATH + "/" + textureFileName);
        return new ImageIcon(resource);
    }
}
