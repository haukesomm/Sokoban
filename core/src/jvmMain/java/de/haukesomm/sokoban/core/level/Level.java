package de.haukesomm.sokoban.core.level;

public record Level(
        String id,
        String name,
        int width,
        int height,
        String layoutString
) { }
