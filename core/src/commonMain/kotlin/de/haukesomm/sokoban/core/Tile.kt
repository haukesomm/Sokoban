package de.haukesomm.sokoban.core

data class Tile(
    val type: TileType,
    val position: Position
) {

    val isTarget: Boolean
        get() = type == TileType.TARGET
}
