package de.haukesomm.sokoban.gui;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.haukesomm.sokoban.Sokoban;
import de.haukesomm.sokoban.LevelManager;
import de.haukesomm.sokoban.SettingsManager;
import de.haukesomm.sokoban.ErrorWindow;
import de.haukesomm.sokoban.gui.gamefield.MoveAction;

public class Game extends JFrame {
    
    public Game() {
        initializeFrame();
        setOSLookAndFeel();
        setKeyBindings();
    }

    private final SettingsManager settings = new SettingsManager();
    private final GridBagConstraints constraints = new GridBagConstraints();

    private MenuBar menu;
    private GameField field;
    private LevelInfoBar levelinfo;
    
    public GameField getGameField() {
        return field;
    }
    
    public LevelInfoBar getLevelInfoBar() {
        return levelinfo;
    }
    
    private void setOSLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Unable to set LAF. Skipping...");
        }
    }

    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(Boolean.valueOf(settings.getValue("resizable")));
        setFocusable(true);
        setOSLookAndFeel();
        setTitle("Sokoban Java (Version " + Sokoban.VERSION + ")");
        setIconImage(new ImageIcon(getClass().getResource("/de/haukesomm/sokoban/resources/textures/box.png")).getImage());
        setLayout(new GridBagLayout());

        menu = new MenuBar(this);
        field = new GameField(this);
        levelinfo = new LevelInfoBar(this);

        setJMenuBar(menu);
        
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        
        constraints.gridy = 1;
        constraints.weighty = 0.7;
        add(field, constraints);
        
        constraints.gridy = 2;
        constraints.weighty = 0.2;
        add(levelinfo, constraints);

        initializeDefaultLevel();
        
        pack();
        setVisible(true);
    }
    
    private void initializeDefaultLevel() {
        try {
            levelinfo.setLevelNames(new LevelManager().getFileList());
            field.initialize(settings.getValue("default") + LevelManager.BUILTIN_EXTENSION);
        } catch (Exception e) {
            new ErrorWindow(this, e).printMessage();
            field.initializeFallback();
        }
    }

    private void setKeyBindings() {
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("UP"), "UP");
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
        
        getRootPane().getActionMap().put("LEFT", new MoveAction(field, 0));
        getRootPane().getActionMap().put("UP", new MoveAction(field, 1));
        getRootPane().getActionMap().put("RIGHT", new MoveAction(field, 2));
        getRootPane().getActionMap().put("DOWN", new MoveAction(field, 3));
    }

}