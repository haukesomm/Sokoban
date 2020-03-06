package sokoban.gui.gamefield;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Target extends JLabel {

    public Target() {
        setIcon(new ImageIcon(getClass().getResource("/sokoban/resources/textures/target.png")));
    }

}
