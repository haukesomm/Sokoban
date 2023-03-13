package de.haukesomm.sokoban.gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

import de.haukesomm.sokoban.gui.gamefield.Box;
import de.haukesomm.sokoban.gui.gamefield.Ground;
import de.haukesomm.sokoban.gui.gamefield.Player;
import de.haukesomm.sokoban.gui.gamefield.Target;
import de.haukesomm.sokoban.gui.gamefield.Wall;
import de.haukesomm.sokoban.level.Level;

public class GameField extends JPanel {
    
    public GameField(Game game) {
        this.game = game;
        setLayout(new GridBagLayout());
    }

    // TODO: Dynamische Spielfeldgröße erlauben
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 16;

    private final GridBagConstraints constraints = new GridBagConstraints();
    private final Game game;
    private final JLabel[][] grid = new JLabel[SIZE_Y][SIZE_X];
    
    private Player player;
    private Box[] boxes;

    private int counter_boxes;
    private int counter_moves;
    private int counter_pushes;

    public Game getGame() {
        return game;
    }
    public JLabel[][] getGrid() {
        return grid;
    }
    public Player getPlayer() {
        return player;
    }
    
    public int getMoves() {
        return counter_moves;
    }
    public int getPushes() {
        return counter_pushes;
    }
    public void counterMovesUp() {
        counter_moves++;
    }
    public void counterPushesUp() {
        counter_pushes++;
    }

    public boolean allBoxesAtTarget() {
        for (int i = 0; i < counter_boxes; i++) {
            if (!boxes[i].getPositionTarget()) {
                return false;
            }
        }
        return true;
    }

    public void initialize(Level level) {
        if (level.width() != SIZE_X || level.height() != SIZE_Y) {
            System.err.println(
                    String.format(
                            "Could not load level: GameField size is {}x{} but level size was {}x{}!",
                            SIZE_X, SIZE_Y, level.width(), level.height()
                    )
            );
            return;
        }

        removeAll();

        String levelString = level.layoutString();

        player = new Player(this);
        boxes = new Box[100];
        counter_boxes = 0;

        // TODO: Symbole aus enum benutzen
        for (int row = 0; row < SIZE_Y; row++) {
            for (int position = 0; position < SIZE_X; position++) {
                constraints.gridy = row;
                constraints.gridx = position;

                switch (levelString.charAt(row * SIZE_X + position)) {
                    case '#':
                        grid[row][position] = new Wall();
                        add(grid[row][position], constraints);
                        break;
                    case '$':
                        boxes[counter_boxes] = new Box(this);
                        boxes[counter_boxes].setPosY(row);
                        boxes[counter_boxes].setPosX(position);

                        grid[row][position] = boxes[counter_boxes];
                        add(grid[row][position], constraints);

                        counter_boxes++;
                        break;
                    case '.':
                        grid[row][position] = new Target();
                        add(grid[row][position], constraints);
                        break;
                    case '@':
                        player.setPosY(row);
                        player.setPosX(position);

                        grid[row][position] = player;
                        add(grid[row][position], constraints);
                        break;
                    default:
                        grid[row][position] = new Ground();
                        add(grid[row][position], constraints);
                        break;
                }
            }
        }

        revalidate();
        repaint();
    }
    
    // Fallback to fill the background if initialization failed
    public void initializeFallback() {
        for (int row = 0; row < SIZE_Y; row++) {
            for (int position = 0; position < SIZE_X; position++) {
                constraints.gridy = row;
                constraints.gridx = position;
                
                player = new Player(this);
                boxes = new Box[100];
                counter_boxes = 0;
                
                grid[row][position] = new Ground();
                add(grid[row][position], constraints);
            }
        }
        
        revalidate();
        repaint();
        
        System.out.println("Successfully initialized fallback level.");
    }

}
