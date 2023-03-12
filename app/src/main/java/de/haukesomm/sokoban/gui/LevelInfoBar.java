package de.haukesomm.sokoban.gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.haukesomm.sokoban.LevelManager;

public class LevelInfoBar extends JPanel {
    
    public LevelInfoBar(Game frame) {
        this.frame = frame;
        initialize();
    }

    private final GridBagConstraints constraints = new GridBagConstraints();

    private final Game frame;
    private JLabel label_moves;
    private JLabel label_pushes;
    private JTextField counter_moves;
    private JTextField counter_pushes;
    private JComboBox<Object> chooser;
    public JButton load;
    
    public void addMove() {
        counter_moves.setText(new DecimalFormat("0000").format(
                Integer.parseInt(counter_moves.getText()) + 1));
    }
    public void addPush() {
        counter_pushes.setText(new DecimalFormat("0000").format(
                Integer.parseInt(counter_pushes.getText()) + 1));
    }
    
    public int getTotalMoves() {
        return Integer.parseInt(counter_moves.getText());
    }
    public int getTotalPushes() {
        return Integer.parseInt(counter_pushes.getText());
    }

    public void setLevelNames(String[] levelNames) {
        for (String s : levelNames) {
            chooser.addItem(s);
        }
    }

    private void initialize() {
        setLayout(new GridBagLayout());

        label_moves = new JLabel("Moves: ");
        label_pushes = new JLabel("Pushes: ");
        
        counter_moves = new JTextField("0000");
        counter_moves.setEditable(false);
        
        counter_pushes = new JTextField("0000");
        counter_pushes.setEditable(false);
        
        chooser = new JComboBox<>();
        chooser.setEditable(false);
        chooser.setMaximumSize(new Dimension(20, Integer.MAX_VALUE));
        load = new JButton("Load Level");
        load.addActionListener((ActionEvent l) -> {
            try {
                frame.getGameField().initialize(chooser.getSelectedItem().toString()
                        + LevelManager.BUILTIN_EXTENSION);
                frame.getRootPane().requestFocus();
            } catch (Exception e) {
                new ErrorWindow(frame, e).show();
                frame.getGameField().initializeFallback();
            }
        });

        constraints.weightx = 1.0;
        constraints.insets = new Insets(3, 3, 3, 3);

        constraints.gridx = 0;
        add(label_moves, constraints);
        constraints.gridx = 1;
        add(counter_moves, constraints);
        constraints.gridx = 2;
        add(label_pushes, constraints);
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
