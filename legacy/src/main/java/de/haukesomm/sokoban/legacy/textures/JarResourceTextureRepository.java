package de.haukesomm.sokoban.legacy.textures;

import de.haukesomm.sokoban.core.model.Tile;

import javax.swing.*;

public class JarResourceTextureRepository implements TextureRepository {

    private static final String BASE_PATH = "/de/haukesomm/sokoban/legacy/textures";

    @Override
    public ImageIcon getForTileType(Tile tileType) {
        String textureFileName = switch (tileType) {
            case Ground -> "ground.png";
            case Wall -> "wall.png";
            case Target -> "target.png";
            case PlayerOnGround, Tile.PlayerOnTarget -> "player.png";
            case BoxOnGround, Tile.BoxOnTarget -> "box.png";
        };

        var resource = getClass().getResource(BASE_PATH + "/" + textureFileName);

        assert resource != null;
        return new ImageIcon(resource);
    }
}
