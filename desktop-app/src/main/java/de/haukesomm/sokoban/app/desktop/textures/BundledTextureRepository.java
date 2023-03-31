package de.haukesomm.sokoban.app.desktop.textures;

import de.haukesomm.sokoban.core.EntityType;
import de.haukesomm.sokoban.core.TileType;

import javax.swing.*;

public class BundledTextureRepository implements TextureRepository {

    private static final String BASE_PATH = "/de/haukesomm/sokoban/app/desktop/textures";

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
