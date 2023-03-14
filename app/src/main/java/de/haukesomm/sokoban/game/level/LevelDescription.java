package de.haukesomm.sokoban.game.level;

public record LevelDescription(String id, String name) {

    @Override
    public String toString() {
        return name;
    }
}
