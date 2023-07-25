package de.haukesomm.sokoban.legacy;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;

import de.haukesomm.sokoban.core.GameStateChangeHandler;
import de.haukesomm.sokoban.core.Position;
import de.haukesomm.sokoban.core.level.LevelToGameStateConverter;
import de.haukesomm.sokoban.core.moving.rules.MultipleBoxesPreventingMoveRule;
import de.haukesomm.sokoban.core.moving.rules.WallCollisionPreventingMoveRule;
import de.haukesomm.sokoban.legacy.level.JarResourceLevelRepository;
import de.haukesomm.sokoban.core.Direction;
import de.haukesomm.sokoban.core.GameStateService;

public class GameFrame extends JFrame {

    private class MovePlayerAction extends AbstractAction {

        private final Direction direction;

        public MovePlayerAction(Direction direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Position position = gameStateService.getPlayerPosition();
            if (position != null) {
                gameStateService.moveEntityIfPossible(position, direction);
            }
        }
    }


    private final GameStateService gameStateService =
            new GameStateService(
                    new JarResourceLevelRepository(20, 16),
                    LevelToGameStateConverter.getDefault(),
                    new WallCollisionPreventingMoveRule(),
                    new MultipleBoxesPreventingMoveRule()
            );

    private final GameField gameField = new GameField();
    private final LevelInfoBar levelInfoBar = new LevelInfoBar(gameStateService.getAvailableLevels());


    public GameFrame() {
        initializeFrame();
        initListeners();
        initializeKeyBindings();
        showFrame();
    }


    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setFocusable(true);

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

    private void initializeKeyBindings() {
        InputMap inputMap = new ComponentInputMap(getRootPane());

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "UP");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "DOWN");

        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put("LEFT", new MovePlayerAction(Direction.Left));
        actionMap.put("UP", new MovePlayerAction(Direction.Top));
        actionMap.put("RIGHT", new MovePlayerAction(Direction.Right));
        actionMap.put("DOWN", new MovePlayerAction(Direction.Bottom));

        getRootPane().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);
    }

    private void initListeners() {
        GameStateChangeHandler.handle(gameStateService, state -> {
            var moves = state.getMoves();
            var pushes = state.getPushes();

            levelInfoBar.setMoveCount(moves);
            levelInfoBar.setPushCount(pushes);

            gameField.drawState(state);

            revalidate();
            pack();
            repaint();
            getRootPane().requestFocus();

            if (state.getLevelCleared()) {
                showLevelClearedDialog(moves, pushes);
            }
        });
        levelInfoBar.addLevelSelectedListener(description -> loadLevel(description.getId()));
    }

    private void loadLevel(String id) {
        levelInfoBar.setMoveCount(0);
        levelInfoBar.setPushCount(0);

        gameStateService.loadLevel(id);
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
