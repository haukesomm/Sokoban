package de.haukesomm.sokoban.core

data class Tile(
    val type: TileType,
    val position: Position,
    val entities: Set<Entity>
) {

    val isTarget: Boolean
        get() = type == TileType.TARGET
}
