package de.haukesomm.sokoban.legacy;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.Position;
import de.haukesomm.sokoban.legacy.textures.JarResourceTextureRepository;
import de.haukesomm.sokoban.legacy.textures.TextureRepository;

public class GameField extends JPanel {

    // TODO: Dynamische Spielfeldgröße erlauben
    private static final int SIZE_X = 20;
    private static final int SIZE_Y = 16;

    private final TextureRepository textureRepository = new JarResourceTextureRepository();

    public GameField() {
        setLayout(new GridBagLayout());
    }

    public void drawState(GameState state) {
        if (state.getWidth() != SIZE_X || state.getHeight() != SIZE_Y) {
            System.err.printf(
                    "Could not load level: GameField size is (%dx%d) but level size was (%dx%d)!%n",
                    SIZE_X, SIZE_Y, state.getWidth(), state.getHeight()
            );
            return;
        }

        removeAll();

        var tiles = state.getTiles();

        for (int row = 0; row < SIZE_Y; row++) {
            for (int column = 0; column < SIZE_X; column++) {
                ImageIcon texture;

                var entity = state.entityAt(new Position(column, row));
                if (entity != null) {
                    texture = textureRepository.getForEntityType(entity.getType(), entity.getFacingDirection());
                } else {
                    var tile = tiles[row][column];
                    texture = textureRepository.getForTileType(tile.getType());
                }

                var jLabel = new JLabel();
                jLabel.setIcon(texture);

                var gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridy = row;
                gridBagConstraints.gridx = column;

                add(jLabel, gridBagConstraints);
            }
        }

        revalidate();
        repaint();
    }
}