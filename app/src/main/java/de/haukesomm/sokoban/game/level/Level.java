package de.haukesomm.sokoban.game.level;

public record Level(
        String id,
        String name,
        int width,
        int height,
        String layoutString
) { }
