package de.haukesomm.sokoban.gui.gamefield;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Target extends JLabel {

    public Target() {
        setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/target.png")));
    }

}
