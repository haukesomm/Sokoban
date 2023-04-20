package de.haukesomm.sokoban.legacy;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import de.haukesomm.sokoban.core.level.DefaultTileFactory;
import de.haukesomm.sokoban.legacy.level.JarResourceLevelRepository;
import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.Entity;
import de.haukesomm.sokoban.core.GameStateService;

public class GameFrame extends JFrame {

    private class MovePlayerAction extends AbstractAction {

        private final Direction direction;

        public MovePlayerAction(Direction direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Entity player = gameStateService.getPlayer();
            if (player != null) {
                gameStateService.moveEntityIfPossible(player, direction);
            }
        }
    }


    private final GameStateService gameStateService =
            new GameStateService(
                    new JarResourceLevelRepository(20, 16),
                    new DefaultTileFactory()
            );

    private final GameField gameField = new GameField();
    private final LevelInfoBar levelInfoBar = new LevelInfoBar(gameStateService.getAvailableLevels());


    public GameFrame() {
        initializeFrame();
        initListeners();
        loadDefaultLevel();
        showFrame();
        getRootPane().requestFocus();
    }


    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setFocusable(true);

        // FIXME: LAF crashes on macOS
        //setLookAndFeel();

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

    private void activateKeyBindings() {
        InputMap inputMap = new ComponentInputMap(getRootPane());

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "UP");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "DOWN");

        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put("LEFT", new MovePlayerAction(Direction.LEFT));
        actionMap.put("UP", new MovePlayerAction(Direction.TOP));
        actionMap.put("RIGHT", new MovePlayerAction(Direction.RIGHT));
        actionMap.put("DOWN", new MovePlayerAction(Direction.BOTTOM));

        getRootPane().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);
    }

    private void disableKeyBindings() {
        getRootPane().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, null);
    }

    private void initListeners() {
        gameStateService.addGameStateChangedListener(state -> {
            var moves = state.getMoves();
            var pushes = state.getPushes();

            levelInfoBar.setMoveCount(moves);
            levelInfoBar.setPushCount(pushes);

            gameField.drawState(state);
            revalidate();
            repaint();

            if (state.getLevelCleared()) {
                disableKeyBindings();
                showLevelClearedDialog(moves, pushes);
            }
        });
        levelInfoBar.addLevelSelectedListener(levelDescription -> loadLevel(levelDescription.getId()));
    }

    private void loadLevel(String id) {
        levelInfoBar.setMoveCount(0);
        levelInfoBar.setPushCount(0);

        gameStateService.loadLevel(id);

        activateKeyBindings();
    }

    private void loadDefaultLevel() {
        var firstLevelId = gameStateService
                .getAvailableLevels()
                .stream()
                .findFirst()
                .orElseThrow().getId();

        loadLevel(firstLevelId);
    }

    private void showFrame() {
        pack();
        setVisible(true);
    }

    private void showLevelClearedDialog(int moves, int pushes) {
        var infoWindow = new InfoWindow(
                this,
                "Level cleared",
                String.format(
                    """
                        <html>
                            <p>Congratulations, you successfully finished this level!</p>
                            <p>You needed %s moves and %s pushes.</p>
                        </html>""",
                    moves,
                    pushes
                )
        );
        infoWindow.display();
    }
}
