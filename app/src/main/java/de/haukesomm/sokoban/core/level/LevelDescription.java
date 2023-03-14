package de.haukesomm.sokoban.core.level;

public record LevelDescription(String id, String name) {

    @Override
    public String toString() {
        return name;
    }
}
