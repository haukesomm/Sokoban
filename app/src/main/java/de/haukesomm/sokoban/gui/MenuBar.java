package de.haukesomm.sokoban.gui;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

    public MenuBar(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        addAll();
    }
    
    private final GameFrame gameFrame;
    
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
            about.addActionListener(l -> new InfoWindow(
                    gameFrame,
                    "About",
                    "<p>Sokoban</p>" +
                            "<p>\u00A9 2016, Hauke Sommerfeld and Daniel Lukic</p>" +
                            "<p>Licensed under the Apache 2.0 license.</p>"
            ));
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
                fileChooser.showOpenDialog(gameFrame);

                try {
                    if (fileChooser.getSelectedFile() != null) {
                        // TODO: Laden von custom levels implementieren
                        // game.getGameField().initialize(fileChooser.getSelectedFile().getName());
                        gameFrame.getRootPane().requestFocus();
                    } else {
                        throw new UnsupportedOperationException("No file selected!");
                    }
                } catch (UnsupportedOperationException e) {
                    new ErrorWindow(gameFrame, e).show();
                    gameFrame.getGameField().initializeFallback();
                }
            });
            add(loadFile);
        }
    }

    private void addAll() {
        add(new SokobanMenu());
        // add(new LevelMenu());
    }
}
