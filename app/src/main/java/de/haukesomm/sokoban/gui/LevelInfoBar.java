package de.haukesomm.sokoban.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.haukesomm.sokoban.game.level.BuiltinLevelRepository;
import de.haukesomm.sokoban.game.level.LevelDescription;
import de.haukesomm.sokoban.game.level.LevelRepository;

public class LevelInfoBar extends JPanel {

    public interface LevelSelectedListener {
        void onLevelSelected(LevelDescription levelDescription);
    }


    private final Set<LevelSelectedListener> levelSelectedListeners = new HashSet<>();

    private final List<LevelDescription> availableLevels;


    private final GridBagConstraints constraints = new GridBagConstraints();

    private JTextField counter_moves;
    private JTextField counter_pushes;
    private JComboBox<LevelDescription> chooser;
    public JButton load;


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
        
        counter_moves = new JTextField("0000");
        counter_moves.setEditable(false);
        
        counter_pushes = new JTextField("0000");
        counter_pushes.setEditable(false);
        
        chooser = new JComboBox<>();
        chooser.setEditable(false);
        chooser.setMaximumSize(new Dimension(20, Integer.MAX_VALUE));
        availableLevels.forEach(chooser::addItem);
        load = new JButton("Load Level");
        load.addActionListener(l -> notifyLevelSelectedListeners((LevelDescription) chooser.getSelectedItem()));

        constraints.weightx = 1.0;
        constraints.insets = new Insets(3, 3, 3, 3);

        constraints.gridx = 0;
        add(movesLabel, constraints);
        constraints.gridx = 1;
        add(counter_moves, constraints);
        constraints.gridx = 2;
        add(pushesLabel, constraints);
        constraints.gridx = 3;
        add(counter_pushes, constraints);
        constraints.gridx = 4;
        constraints.ipadx = 150;
        add(chooser, constraints);
        constraints.gridx = 5;
        constraints.ipadx = 0;
        add(load, constraints);
    }
}
