package de.haukesomm.sokoban.app.desktop.textures;

import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.EntityType;
import de.haukesomm.sokoban.core.TileType;
import org.jetbrains.annotations.Nullable;

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
    public ImageIcon getForEntityType(EntityType entityType, Direction direction) {
        String textureFileName = switch (entityType) {
            case BOX -> "box.png";
            case PLAYER -> getPlayerTextureFilenameForDirection(direction);
        };
        var resource = getClass().getResource(BASE_PATH + "/" + textureFileName);
        return new ImageIcon(resource);
    }

    private String getPlayerTextureFilenameForDirection(Direction direction) {
        return switch (direction) {
            case TOP -> "player-top.png";
            case RIGHT -> "player-right.png";
            case BOTTOM -> "player-bottom.png";
            case LEFT -> "player-left.png";
        };
    }
}
