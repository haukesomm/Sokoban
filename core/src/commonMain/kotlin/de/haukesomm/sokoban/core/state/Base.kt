package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.Tile
import kotlinx.serialization.Serializable

interface GameState {
    val levelId: String
    val width: Int
    val height: Int
    val tiles: List<Tile>
    val moves: Int
    val pushes: Int
    val levelCleared: Boolean

    fun tileAt(position: Position): Tile? =
        tiles.getOrNull(position.toIndex(width))

    fun tileInDirection(position: Position, direction: Direction): Tile? =
        tileAt(position.nextInDirection(direction))

    fun entityAt(position: Position): Entity? =
        tileAt(position)?.entity

    fun entityInDirection(position: Position, direction: Direction): Entity? =
        entityAt(position.nextInDirection(direction))

    fun getPlayerPosition(): Position? =
        when (val index = tiles.indexOfFirst { it.entity?.isPlayer == true }) {
            -1 -> null
            else -> Position.fromIndex(index, width)
        }
}