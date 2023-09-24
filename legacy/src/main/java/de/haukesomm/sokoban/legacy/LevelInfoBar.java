package de.haukesomm.sokoban.legacy;

import de.haukesomm.sokoban.core.LevelDescription;
import de.haukesomm.sokoban.legacy.level.LevelDescriptionListCellRenderer;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelInfoBar extends JPanel {

    public interface LevelSelectedListener {
        void onLevelSelected(LevelDescription levelDescription);
    }


    private final Set<LevelSelectedListener> levelSelectedListeners = new HashSet<>();

    private final List<LevelDescription> availableLevels;


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
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        var movesLabel = new JLabel("Moves: ");
        var pushesLabel = new JLabel("Pushes: ");

        moveCounterTextField = new JTextField("0000");
        moveCounterTextField.setEditable(false);

        pushCounterTextField = new JTextField("0000");
        pushCounterTextField.setEditable(false);

        var levelComboBox = new JComboBox<LevelDescription>();
        levelComboBox.setEditable(false);
        levelComboBox.setRenderer(new LevelDescriptionListCellRenderer());
        availableLevels.forEach(levelComboBox::addItem);

        var loadButton = new JButton("Load");
        loadButton.addActionListener(l -> notifyLevelSelectedListeners((LevelDescription) levelComboBox.getSelectedItem()));


        var box = Box.createHorizontalBox();

        box.add(movesLabel);

        moveCounterTextField.setMaximumSize(moveCounterTextField.getPreferredSize());
        box.add(moveCounterTextField);

        box.add(Box.createHorizontalStrut(10));

        box.add(pushesLabel);

        pushCounterTextField.setMaximumSize(pushCounterTextField.getPreferredSize());
        box.add(pushCounterTextField);

        box.add(Box.createHorizontalGlue());

        levelComboBox.setMaximumSize(levelComboBox.getPreferredSize());
        box.add(levelComboBox);
        box.add(loadButton);


        add(Box.createHorizontalStrut(10));
        add(box);
        add(Box.createHorizontalStrut(10));
    }

    public void setMoveCount(int count) {
        moveCounterTextField.setText(String.format("%04d", count));
    }

    public void setPushCount(int count) {
        pushCounterTextField.setText(String.format("%04d", count));
    }
}
