package de.haukesomm.sokoban.legacy;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

import de.haukesomm.sokoban.core.LevelDescription;
import de.haukesomm.sokoban.legacy.level.LevelDescriptionListCellRenderer;

public class LevelInfoBar extends JPanel {

    public interface LevelSelectedListener {
        void onLevelSelected(LevelDescription levelDescription);
    }


    private final Set<LevelSelectedListener> levelSelectedListeners = new HashSet<>();

    private final List<LevelDescription> availableLevels;


    private final GridBagConstraints constraints = new GridBagConstraints();

    private JTextField moveCounterTextField;

    private JTextField pushCounterTextField;


    public LevelInfoBar(List<LevelDescription> availableLevels) {
        this.availableLevels = availableLevels;
        initialize();
    }


    private void notifyLevelSelectedListeners(LevelDescription selected) {
        levelSelectedListeners.forEach(l -> l.onLevelSelected(selected));
    }

    public void addLevelSelectedListener(LevelSelectedListener listener) {
        levelSelectedListeners.add(listener);
    }


    private void initialize() {
        setLayout(new GridBagLayout());

        var movesLabel = new JLabel("Moves: ");
        var pushesLabel = new JLabel("Pushes: ");
        
        moveCounterTextField = new JTextField("0000");
        moveCounterTextField.setEditable(false);
        
        pushCounterTextField = new JTextField("0000");
        pushCounterTextField.setEditable(false);

        var levelComboBox = new JComboBox<LevelDescription>();
        levelComboBox.setEditable(false);
        levelComboBox.setMaximumSize(new Dimension(20, Integer.MAX_VALUE));
        levelComboBox.setRenderer(new LevelDescriptionListCellRenderer());
        availableLevels.forEach(levelComboBox::addItem);

        var loadButton = new JButton("Load Level");
        loadButton.addActionListener(l ->
                notifyLevelSelectedListeners((LevelDescription) levelComboBox.getSelectedItem()));

        constraints.weightx = 1.0;
        constraints.insets = new Insets(3, 3, 3, 3);

        constraints.gridx = 0;
        add(movesLabel, constraints);

        constraints.gridx = 1;
        add(moveCounterTextField, constraints);

        constraints.gridx = 2;
        add(pushesLabel, constraints);

        constraints.gridx = 3;
        add(pushCounterTextField, constraints);

        constraints.gridx = 4;
        constraints.ipadx = 150;
        add(levelComboBox, constraints);

        constraints.gridx = 5;
        constraints.ipadx = 0;
        add(loadButton, constraints);
    }

    public void setMoveCount(int count) {
        moveCounterTextField.setText(String.format("%04d", count));
    }

    public void setPushCount(int count) {
        pushCounterTextField.setText(String.format("%04d", count));
    }
}
