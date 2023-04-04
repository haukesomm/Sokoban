package de.haukesomm.sokoban.web.level

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType
import de.haukesomm.sokoban.core.level.LevelCharacterMap

class BundledLevelCharacterMap : LevelCharacterMap {

    override fun getTileType(character: Char): TileType =
        when(character) {
            '#' -> TileType.WALL
            '.' -> TileType.TARGET
            else -> TileType.NOTHING
        }

    override fun getEntityType(character: Char): EntityType? =
        when(character) {
            '@' -> EntityType.PLAYER
            'X' -> EntityType.BOX
            else -> null
        }
}