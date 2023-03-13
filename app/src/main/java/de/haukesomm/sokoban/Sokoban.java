package de.haukesomm.sokoban;

import de.haukesomm.sokoban.gui.Game;

public class Sokoban {
    
    public static void main(String[] args) {
        final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
        new Game();
    }
}
