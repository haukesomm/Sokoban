package de.haukesomm.sokoban.core

import kotlinx.serialization.Serializable

enum class TileType {
    Empty,
    Wall,
    Target
}

/**
 * Represents a tile on the game board.
 *
 * A tile can hold an [Entity] and has a [TileType] that determines if it is a wall, a target or empty.
 */
@Serializable
data class Tile(
    val type: TileType,
    val entity: Entity? = null
) {
    val isTarget: Boolean = type == TileType.Target
    val isWall: Boolean = type == TileType.Wall
}
