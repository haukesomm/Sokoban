package de.haukesomm.sokoban.gui.gamefield;

import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;

import de.haukesomm.sokoban.gui.GameField;

public class Player extends MovableObject {

    public Player(GameField field) {
        super(field);
        setDirection(0);
    }
    
    private final GridBagConstraints constraints = new GridBagConstraints();
    
    private int direction;

    public void setDirection(int direction) {
        this.direction = direction;
        
        switch (direction) {
            case 0:
                setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/player0.png")));
                break;
            case 1:
                setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/player1.png")));
                break;
            case 2:
                setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/player2.png")));
                break;
            case 3:
                setIcon(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/player3.png")));
                break;
        }
        
        field.revalidate();
        field.repaint();
    }

    public void move() {
        int oldY = getPosY();
        int oldX = getPosX();

        int newY = 0;
        int newYY = 0;
        int newX = 0;
        int newXX = 0;

        switch (direction) {
            case 0:
                newY = oldY;
                newYY = oldY;
                newX = oldX - 1;
                newXX = newX - 1;
                break;
            case 1:
                newY = oldY - 1;
                newYY = newY - 1;
                newX = oldX;
                newXX = oldX;
                break;
            case 2:
                newY = oldY;
                newYY = oldY;
                newX = oldX + 1;
                newXX = newX + 1;
                break;
            case 3:
                newY = oldY + 1;
                newYY = newY + 1;
                newX = oldX;
                newXX = oldX;
                break;
        }

        if (field.getGrid()[newY][newX] instanceof Ground
                || field.getGrid()[newY][newX] instanceof Box
                || field.getGrid()[newY][newX] instanceof Target) {

            if (field.getGrid()[newY][newX] instanceof Box
                    && !(field.getGrid()[newYY][newXX] instanceof Wall)
                    && !(field.getGrid()[newYY][newXX] instanceof Box)) {

                Box tmp = (Box) field.getGrid()[newY][newX];
                tmp.move(direction);
            }

            if ((!(field.getGrid()[newY][newX] instanceof Wall)
                    && !(field.getGrid()[newY][newX] instanceof Box))
                    || (field.getGrid()[newY][newX] instanceof Box
                    && !(field.getGrid()[newYY][newXX] instanceof Box)
                    && !(field.getGrid()[newYY][newXX] instanceof Wall))) {

                boolean tmp_boolean;
                tmp_boolean = field.getGrid()[newY][newX] instanceof Target;

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

                setPositionTarget(tmp_boolean);

                field.counterMovesUp();
            }
        }
        
        field.getGame().getLevelInfoBar().addMove();
        
        field.revalidate();
        field.repaint();
    }

}
