package de.haukesomm.sokoban.legacy;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import de.haukesomm.sokoban.core.GameState;
import de.haukesomm.sokoban.core.Position;
import de.haukesomm.sokoban.legacy.textures.JarResourceTextureRepository;
import de.haukesomm.sokoban.legacy.textures.TextureRepository;

public class GameField extends JPanel {

    private final TextureRepository textureRepository = new JarResourceTextureRepository();

    public GameField() {
        setLayout(new GridBagLayout());
    }

    public void drawState(GameState state) {
        removeAll();

        for (int row = 0; row < state.getHeight(); row++) {
            for (int column = 0; column < state.getWidth(); column++) {
                var position = new Position(column, row);
                ImageIcon texture;

                var entity = state.entityAt(position);
                if (entity != null) {
                    texture = textureRepository.getForEntityType(entity.getType());
                } else {
                    var tile = state.tileAt(position);
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
