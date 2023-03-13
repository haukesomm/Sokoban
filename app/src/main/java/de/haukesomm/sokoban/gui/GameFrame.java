package de.haukesomm.sokoban.gui;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.haukesomm.sokoban.gui.gamefield.MoveAction;
import de.haukesomm.sokoban.level.BuiltinLevelRepository;
import de.haukesomm.sokoban.level.LevelRepository;

public class GameFrame extends JFrame {

    private final LevelRepository levelRepository = new BuiltinLevelRepository();

    private GameField gameField;
    private LevelInfoBar levelinfo;

    public GameFrame() {
        initializeFrame();
    }

    public GameField getGameField() {
        return gameField;
    }
    
    public LevelInfoBar getLevelInfoBar() {
        return levelinfo;
    }

    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);

        setLookAndFeel();
        setKeyBindings();

        setTitle("Sokoban");
        setIconImage(new ImageIcon(
                getClass().getResource("/de/haukesomm/sokoban/resources/textures/box.png")).getImage());
        setLayout(new GridBagLayout());

        var menu = new MenuBar(this);
        setJMenuBar(menu);


        var constraints = new GridBagConstraints();
        
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;

        gameField = new GameField(this);
        constraints.gridy = 1;
        constraints.weighty = 0.7;
        add(gameField, constraints);

        levelinfo = new LevelInfoBar(this);
        constraints.gridy = 2;
        constraints.weighty = 0.2;
        add(levelinfo, constraints);

        initializeDefaultLevel();


        pack();
        setVisible(true);
    }
    
    private void initializeDefaultLevel() {
        var levels = levelRepository.getAvailableLevels();
        levelinfo.setLevelNames(levels);
        gameField.initialize(levelRepository.getLevelOrNull(levels.get(0).id()));
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException |
                 UnsupportedLookAndFeelException e
        ) {
            System.err.println("Unable to set LAF:");
            e.printStackTrace();
        }
    }

    private void setKeyBindings() {
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("UP"), "UP");
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
        
        getRootPane().getActionMap().put("LEFT", new MoveAction(gameField, 0));
        getRootPane().getActionMap().put("UP", new MoveAction(gameField, 1));
        getRootPane().getActionMap().put("RIGHT", new MoveAction(gameField, 2));
        getRootPane().getActionMap().put("DOWN", new MoveAction(gameField, 3));
    }
}
