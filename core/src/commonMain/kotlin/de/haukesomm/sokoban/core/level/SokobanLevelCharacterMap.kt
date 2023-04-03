package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType

class SokobanLevelCharacterMap : LevelCharacterMap {

    override fun getTileType(character: Char): TileType? {
        return when (character) {
            '#' -> TileType.WALL
            '.' -> TileType.TARGET
            else -> TileType.NOTHING
        }
    }

    override fun getEntityType(character: Char): EntityType? {
        return when (character) {
            '@' -> EntityType.PLAYER
            '$' -> EntityType.BOX
            else -> null
        }
    }
}
