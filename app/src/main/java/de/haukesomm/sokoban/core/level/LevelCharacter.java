package de.haukesomm.sokoban.core.level;

public enum LevelCharacter {
    NOTHING('_'),
    WALL('#'),
    BOX('$'),
    TARGET('.'),
    PLAYER('@');

    private final char levelFileCharacter;

    LevelCharacter(char levelFileCharacter) {
        this.levelFileCharacter = levelFileCharacter;
    }

    public char getLevelFileCharacter() {
        return levelFileCharacter;
    }
}