package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType

interface LevelCharacterMap {

    fun getTileType(character: Char): TileType?

    fun getEntityType(character: Char): EntityType?
}
