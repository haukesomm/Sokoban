package de.haukesomm.sokoban.gui.gamefield;

import java.awt.GridBagConstraints;
import javax.swing.*;

import de.haukesomm.sokoban.gui.GameField;

public class Player extends JLabel {

    public Player() {
        setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/player0.png")));

    }
}