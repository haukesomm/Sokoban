package de.haukesomm.sokoban.gui.gamefield;

import de.haukesomm.sokoban.gui.GameField;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class MoveAction extends AbstractAction {

    public MoveAction(GameField field, int direction) {
        this.field = field;
        this.direction = direction;
    }
    
    private final GameField field;
    private final int direction;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        field.getPlayer().setDirection(direction);
        field.getPlayer().move();        
    }
    
}
