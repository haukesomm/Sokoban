package de.haukesomm.sokoban.core

data class Tile(
    val type: Type,
    val position: Position,
    val entities: Set<Entity> = emptySet()
) {
    @Suppress("unused")
    enum class Type { Empty, Wall, Target }

    val isTarget: Boolean
        get() = type == Type.Target

    val isWall: Boolean
        get() = type == Type.Wall
}
