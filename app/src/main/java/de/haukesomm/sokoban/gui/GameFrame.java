package de.haukesomm.sokoban.gui;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.Optional;

import de.haukesomm.sokoban.game.Direction;
import de.haukesomm.sokoban.game.Entity;
import de.haukesomm.sokoban.game.GameStateService;

public class GameFrame extends JFrame {

    private class MovePlayerAction extends AbstractAction {

        private Direction direction;

        public MovePlayerAction(Direction direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Optional<Entity> player = gameStateService.getPlayer();
            player.ifPresent(entity -> gameStateService.moveEntityIfPossible(entity, direction));
        }
    }


    private final GameStateService gameStateService = new GameStateService();

    private final GameField gameField = new GameField();
    private final LevelInfoBar levelInfoBar = new LevelInfoBar(gameStateService.getAvailableLevels());;


    public GameFrame() {
        initializeFrame();
        initListeners();
        loadDefaultLevel();
        showFrame();
    }


    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setFocusable(true);

        // FIXME: LAF crashes on macOS
        //setLookAndFeel();
        setKeyBindings();

        setTitle("Sokoban");
        setLayout(new GridBagLayout());

        var menu = new MenuBar(this);
        setJMenuBar(menu);


        var constraints = new GridBagConstraints();
        
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;

        constraints.gridy = 1;
        constraints.weighty = 0.7;
        add(gameField, constraints);

        constraints.gridy = 2;
        constraints.weighty = 0.2;
        add(levelInfoBar, constraints);
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
        InputMap inputMap = getRootPane().getInputMap();
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "UP");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "DOWN");

        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put("LEFT", new MovePlayerAction(Direction.LEFT));
        actionMap.put("UP", new MovePlayerAction(Direction.TOP));
        actionMap.put("RIGHT", new MovePlayerAction(Direction.RIGHT));
        actionMap.put("DOWN", new MovePlayerAction(Direction.BOTTOM));
    }

    private void initListeners() {
        gameStateService.addGameStateChangedListener((state, moves, pushes) -> {
            levelInfoBar.setMoveCount(moves);
            levelInfoBar.setPushCount(pushes);

            gameField.drawState(state);
            revalidate();
            repaint();
            getRootPane().requestFocus();
        });
        levelInfoBar.addLevelSelectedListener(levelDescription -> gameStateService.loadLevel(levelDescription.id()));
    }

    private void loadDefaultLevel() {
        var firstLevelId = gameStateService
                .getAvailableLevels()
                .stream()
                .findFirst()
                .orElseThrow()
                .id();

        gameStateService.loadLevel(firstLevelId);
    }

    private void showFrame() {
        pack();
        setVisible(true);
    }
}
