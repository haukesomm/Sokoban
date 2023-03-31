package de.haukesomm.sokoban.core;

public record Position(int x, int y) {

    public static Position fromIndex(int index, int width) {
        int x = index % width;
        int y = (int) Math.floor(((double) index) / width);
        return new Position(x, y);
    }

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
