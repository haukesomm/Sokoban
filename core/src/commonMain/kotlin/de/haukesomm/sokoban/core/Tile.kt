package de.haukesomm.sokoban.core

import kotlinx.serialization.Serializable

enum class TileType {
    Empty,
    Wall,
    Target
}

@Serializable
data class Tile(
    val type: TileType,
    val entity: Entity? = null
) {
    val isTarget: Boolean
        get() = type == TileType.Target

    val isWall: Boolean
        get() = type == TileType.Wall
}
