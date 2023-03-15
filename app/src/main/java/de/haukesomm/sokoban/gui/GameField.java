package de.haukesomm.sokoban.gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import de.haukesomm.sokoban.game.GameState;
import de.haukesomm.sokoban.game.Position;
import de.haukesomm.sokoban.gui.gamefield.Box;
import de.haukesomm.sokoban.gui.gamefield.Ground;
import de.haukesomm.sokoban.gui.gamefield.Player;
import de.haukesomm.sokoban.gui.gamefield.Target;
import de.haukesomm.sokoban.gui.gamefield.Wall;

public class GameField extends JPanel {

    // TODO: Dynamische Spielfeldgröße erlauben
    private static final int SIZE_X = 20;
    private static final int SIZE_Y = 16;

    public GameField() {
        setLayout(new GridBagLayout());
    }

    public void drawState(GameState state) {
        if (state.getMapWidth() != SIZE_X || state.getMapHeight() != SIZE_Y) {
            System.err.printf(
                    "Could not load level: GameField size is (%dx%d) but level size was (%dx%d)!%n",
                    SIZE_X, SIZE_Y, state.getMapWidth(), state.getMapWidth()
            );
            return;
        }

        removeAll();

        var tiles = state.tiles();

        for (int row = 0; row < SIZE_Y; row++) {
            for (int column = 0; column < SIZE_X; column++) {
                var gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridy = row;
                gridBagConstraints.gridx = column;

                var entity = state.getEntityAtPositionOrNull(new Position(column, row));
                if (entity != null) {
                    switch (entity.type()) {
                        case PLAYER -> add(new Player(), gridBagConstraints);
                        case BOX -> add(new Box(), gridBagConstraints);
                    }
                    continue;
                }

                var tile = tiles[row][column];
                switch (tile.type()) {
                    case WALL -> add(new Wall(), gridBagConstraints);
                    case TARGET -> add(new Target(), gridBagConstraints);
                    case NOTHING -> add(new Ground(), gridBagConstraints);
                }
            }
        }

        revalidate();
        repaint();
    }
}
