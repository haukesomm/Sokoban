package sokoban.gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.FileNotFoundException;
import javax.swing.JLabel;

import sokoban.LevelManager;
import sokoban.gui.gamefield.Box;
import sokoban.gui.gamefield.Ground;
import sokoban.gui.gamefield.Player;
import sokoban.gui.gamefield.Target;
import sokoban.gui.gamefield.Wall;

public class GameField extends JPanel {
    
    public GameField(Game game) {
        this.game = game;
        setLayout(new GridBagLayout());
    }

    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 20;
    
    private final GridBagConstraints constraints = new GridBagConstraints();
    private final Game game;
    private final JLabel[][] grid = new JLabel[SIZE_X][SIZE_Y];
    
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

    public void initialize(String level) throws FileNotFoundException {
        removeAll();

        String levelString = new LevelManager().getLevelData(level); // FNF Exception

        player = new Player(this);
        boxes = new Box[100];
        counter_boxes = 0;

        for (int row = 0; row < SIZE_X; row++) {
            for (int position = 0; position < SIZE_Y; position++) {
                constraints.gridy = row;
                constraints.gridx = position;

                switch (levelString.charAt(row * SIZE_Y + position)) {
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
        
        System.out.println("Sucessfully initialized level '" + level + "'.");
    }
    
    // Fallback to fill the background if initialization failed
    public void initializeFallback() {
        for (int row = 0; row < SIZE_X; row++) {
            for (int position = 0; position < SIZE_Y; position++) {
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
