package de.haukesomm.sokoban.core

data class Tile(
    val type: TileType,
    val position: Position,
    val entities: Set<Entity> = emptySet()
) {

    val isTarget: Boolean
        get() = type == TileType.TARGET
}
