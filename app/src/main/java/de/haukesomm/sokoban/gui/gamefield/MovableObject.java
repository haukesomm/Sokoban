package de.haukesomm.sokoban.gui.gamefield;

import javax.swing.JLabel;

import de.haukesomm.sokoban.gui.GameField;

public abstract class MovableObject extends JLabel {
    
    public MovableObject(GameField field) {
        this.field = field;
    }
    
    protected final GameField field;
    private int position_x;
    private int position_y;
    private boolean position_target = false;
    
    public int getPosY() {
        return position_x;
    }

    public int getPosX() {
        return position_y;
    }

    public void setPosY(int y) {
        this.position_x = y;
    }

    public void setPosX(int x) {
        this.position_y = x;
    }

    public boolean getPositionTarget() {
        return position_target;
    }

    public void setPositionTarget(boolean position_target) {
        this.position_target = position_target;
    }
    
}
