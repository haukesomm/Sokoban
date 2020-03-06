package sokoban.gui.gamefield;

import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;

import sokoban.gui.GameField;
import sokoban.gui.InfoWindow;

public class Box extends MovableObject {

    public Box(GameField field) {
        super(field);
        setIcon(new ImageIcon(getClass().getResource("/sokoban/resources/textures/box.png")));
    }
    
    private final GridBagConstraints constraints = new GridBagConstraints();
    
    public void move(int direction) {
        int oldY = getPosY();
        int oldX = getPosX();

        int newY = 0;
        int newX = 0;
        switch (direction) {
            case 0:
                newY = oldY;
                newX = oldX - 1;
                break;
            case 1:
                newY = oldY - 1;
                newX = oldX;
                break;
            case 2:
                newY = oldY;
                newX = oldX + 1;
                break;
            case 3:
                newY = oldY + 1;
                newX = oldX;
                break;
        }

        if (field.getGrid()[newY][newX] instanceof Ground
                || field.getGrid()[newY][newX] instanceof Target) {

            boolean target_tmp = field.getGrid()[newY][newX] instanceof Target;
            
            constraints.gridy = oldY;
            constraints.gridx = oldX;
            field.remove(field.getGrid()[oldY][oldX]);
            if (getPositionTarget()) {
                field.getGrid()[oldY][oldX] = new Target();
            } else {
                field.getGrid()[oldY][oldX] = new Ground();
            }
            field.add(field.getGrid()[oldY][oldX], constraints);

            constraints.gridy = newY;
            constraints.gridx = newX;
            field.remove(field.getGrid()[newY][newX]);
            field.getGrid()[newY][newX] = this;
            field.add(field.getGrid()[newY][newX], constraints);
            setPosY(newY);
            setPosX(newX);

            setPositionTarget(target_tmp);
            
            field.getGame().getLevelInfoBar().addPush();

            field.revalidate();
            field.repaint();
        }

        field.counterPushesUp();
        
        if (field.allBoxesAtTarget()) {
            new InfoWindow(field, "Gratulations!",
                    "You successfully finishes this level!"
                    + "<br><br>You needed " + field.getMoves() + " turns"
                    + "<br>and pushed boxes " + field.getPushes() + " times.");
        }
    }

}
