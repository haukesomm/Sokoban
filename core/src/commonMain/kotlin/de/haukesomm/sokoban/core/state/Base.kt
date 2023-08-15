package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.*
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

    fun print(): String =
        buildString {
            tiles.forEachIndexed { index, tile ->
                val char = when (val entity = tile.entity) {
                    null -> when (tile.type) {
                        TileType.Wall -> '#'
                        TileType.Target -> '.'
                        TileType.Empty -> ' '
                    }
                    else -> when(entity.type) {
                        EntityType.Player -> '@'
                        EntityType.Box -> '$'
                    }
                }
                if (index % width == 0) {
                    append("\n")
                    append(char)
                }
                else append(char)
            }
        }
}