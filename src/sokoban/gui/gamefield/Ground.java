package sokoban.gui.gamefield;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Ground extends JLabel {

    public Ground() {
        setIcon(new ImageIcon(getClass().getResource("/sokoban/resources/textures/ground.png")));
    }

}
