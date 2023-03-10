package de.haukesomm.sokoban.gui;

import java.io.FileNotFoundException;
import de.haukesomm.sokoban.ErrorWindow;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.haukesomm.sokoban.Sokoban;

public class MenuBar extends JMenuBar {

    public MenuBar(Game game) {
        this.game = game;
        addAll();
    }
    
    private final Game game;
    
    private abstract class AbstractMenu extends JMenu {
        
        public AbstractMenu(String title) {
            super(title);
            addEntries();
        }
        
        public abstract void addEntries();
        
    }
    
    private class SokobanMenu extends AbstractMenu {
        
        public SokobanMenu() {
            super("Sokoban");
        }
        
        @Override
        public void addEntries() {
            JMenuItem about = new JMenuItem("About");
            about.addActionListener(l -> {
                new InfoWindow(game, "About",
                        "Sokoban Java Clone Version " + Sokoban.VERSION
                        + "<br><br>\u00A9 2016, Hauke Sommerfeld and Daniel Lukic"
                        + "<br>\nLicensed under the Apache 2.0 license.");
            });
            add(about);
            
            addSeparator();
            
            JMenuItem exit = new JMenuItem("Exit Game");
            exit.addActionListener(l -> {
                System.exit(0);
            });
            add(exit);
        }
        
    }
    
    private class LevelMenu extends AbstractMenu {
        
        public LevelMenu() {
            super("Level");
        }
        
        @Override
        public void addEntries() {
            JMenuItem loadFile = new JMenuItem("Load Custom Level (.custom)");
            loadFile.addActionListener(l -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open custom level file");
                fileChooser.showOpenDialog(game);

                try {
                    if (fileChooser.getSelectedFile() != null) {
                        game.getGameField().initialize(fileChooser.getSelectedFile().getName());
                        game.getRootPane().requestFocus();
                    } else {
                        throw new UnsupportedOperationException("No file selected!");
                    }
                } catch (UnsupportedOperationException | FileNotFoundException e) {
                    new ErrorWindow(game, e).show();
                    game.getGameField().initializeFallback();
                }
            });
            add(loadFile);
        }
        
    }

    private void addAll() {
        add(new SokobanMenu());
        add(new LevelMenu());
    }

}
