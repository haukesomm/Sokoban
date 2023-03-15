package de.haukesomm.sokoban.gui.gamefield;

import java.awt.GridBagConstraints;
import javax.swing.*;

import de.haukesomm.sokoban.gui.GameField;
import de.haukesomm.sokoban.gui.InfoWindow;

public class Box extends JLabel {

    public Box() {
        setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/box.png")));
    }
}
