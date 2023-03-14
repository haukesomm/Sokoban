package de.haukesomm.sokoban.game;

public record Position(int x, int y) {

    public Position nextInDirection(Direction direction) {
        int nextX = x;
        int nextY = y;

        switch (direction) {
            case TOP -> nextY = y - 1;
            case RIGHT -> nextX = x + 1;
            case BOTTOM -> nextY = y + 1;
            case LEFT -> nextX = x - 1;
        }

        return new Position(nextX, nextY);
    }
}
