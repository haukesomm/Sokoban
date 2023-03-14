package de.haukesomm.sokoban.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.haukesomm.sokoban.game.level.BuiltinLevelRepository;
import de.haukesomm.sokoban.game.level.LevelDescription;
import de.haukesomm.sokoban.game.level.LevelRepository;

public class LevelInfoBar extends JPanel {
    
    public LevelInfoBar(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        initialize();
    }

    private final LevelRepository levelRepository = new BuiltinLevelRepository();

    private final GridBagConstraints constraints = new GridBagConstraints();

    private final GameFrame gameFrame;
    private JTextField counter_moves;
    private JTextField counter_pushes;
    private JComboBox<LevelDescription> chooser;
    public JButton load;
    
    public void addMove() {
        counter_moves.setText(new DecimalFormat("0000").format(
                Integer.parseInt(counter_moves.getText()) + 1));
    }
    public void addPush() {
        counter_pushes.setText(new DecimalFormat("0000").format(
                Integer.parseInt(counter_pushes.getText()) + 1));
    }

    public void setLevelNames(List<LevelDescription> levels) {
        levels.forEach(chooser::addItem);
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
        load = new JButton("Load Level");
        load.addActionListener(l -> {
            var selectedLevelDescription = (LevelDescription) chooser.getSelectedItem();
            var selectedLevel = levelRepository.getLevelOrNull(selectedLevelDescription.id());
            gameFrame.getGameField().initialize(selectedLevel);
            gameFrame.getRootPane().requestFocus();
        });

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
