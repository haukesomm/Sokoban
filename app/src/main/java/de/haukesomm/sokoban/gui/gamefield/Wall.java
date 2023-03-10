package de.haukesomm.sokoban.gui.gamefield;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Wall extends JLabel {

    public Wall() {
        setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/wall.png")));
    }

}
