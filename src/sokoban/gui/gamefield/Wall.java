package sokoban.gui.gamefield;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Wall extends JLabel {

    public Wall() {
        setIcon(new ImageIcon(getClass().getResource("/sokoban/resources/textures/wall.png")));
    }

}
