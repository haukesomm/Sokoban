package de.haukesomm.sokoban.legacy.textures;

import de.haukesomm.sokoban.core.EntityType;
import de.haukesomm.sokoban.core.TileType;

import javax.swing.*;

public class JarResourceTextureRepository implements TextureRepository {

    private static final String BASE_PATH = "/de/haukesomm/sokoban/legacy/textures";

    @Override
    public ImageIcon getForTileType(TileType tileType) {
        String textureFileName = switch (tileType) {
            case Empty -> "ground.png";
            case Wall -> "wall.png";
            case Target -> "target.png";
        };
        var resource = getClass().getResource(BASE_PATH + "/" + textureFileName);
        return new ImageIcon(resource);
    }

    @Override
    public ImageIcon getForEntityType(EntityType entityType) {
        String textureFileName = switch (entityType) {
            case Box -> "box.png";
            case Player -> "player.png";
        };
        var resource = getClass().getResource(BASE_PATH + "/" + textureFileName);
        return new ImageIcon(resource);
    }
}
