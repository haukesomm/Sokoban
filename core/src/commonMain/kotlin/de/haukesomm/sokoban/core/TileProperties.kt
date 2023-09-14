package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType

/**
 * Represents both the type of tile and the type of entity that may be present on the tile.
 * The latter is optional and may be `null` if the tile does not contain an entity.
 */
data class TileProperties(
    val tileType: TileType,
    val entityType: EntityType? = null
)