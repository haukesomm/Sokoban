package de.haukesomm.sokoban.level;

public record LevelDescription(String id, String name) {

    @Override
    public String toString() {
        return name;
    }
}
