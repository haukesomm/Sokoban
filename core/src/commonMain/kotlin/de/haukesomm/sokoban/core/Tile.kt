package de.haukesomm.sokoban.core

import kotlinx.serialization.Serializable

enum class TileType { Empty, Wall, Target }

/**
 * Represents both the type of tile and the type of entity that may be present on the tile.
 *
 * This class is mainly used in [CharacterMap] to map characters to the characteristics of a [Tile].
 * The [entityType] may be `null` if the tile does not contain an [Entity].
 */
data class TileProperties(
    val tileType: TileType,
    val entityType: EntityType? = null
)

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

    companion object {

        /**
         * Creates a new [Tile] from the given [TileProperties].
         */
        fun fromTileProperties(tileProperties: TileProperties): Tile =
            Tile(tileProperties.tileType, tileProperties.entityType?.let(::Entity))
    }
}
