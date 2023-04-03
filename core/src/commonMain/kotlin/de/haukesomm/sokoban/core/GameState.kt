package de.haukesomm.sokoban.core

import kotlin.jvm.JvmField
import kotlin.jvm.JvmRecord

@JvmRecord
data class GameState(
    val tiles: Array<Array<Tile>>,
    val entities: Collection<Entity>
) {
    fun getMapWidth(): Int = tiles[0].size

    fun getMapHeight(): Int = tiles.size

    fun getNextTileInDirectionOrNull(position: Position, direction: Direction?): Tile? {
        val (x, y) = position.nextInDirection(direction!!)
        return if (y > tiles.size || tiles.size > 0 && x > tiles[0].size) {
            null
        } else tiles[y][x]
    }

    fun getEntityAtPositionOrNull(position: Position): Entity? {
        for (entity in entities) {
            if (entity.position == position) {
                return entity
            }
        }
        return null
    }

    fun getEntityAtNextPositionOrNull(position: Position, direction: Direction): Entity? =
        getEntityAtPositionOrNull(position.nextInDirection(direction))

    fun getPlayer(): Entity? = entities.find { it.type == EntityType.PLAYER }

    fun getEntityById(id: String): Entity? = entities.find { it.id == id }
}
