package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.*

interface GameState {
    val levelId: String
    val width: Int
    val height: Int
    val tiles: List<Tile>
    val entities: Set<Entity>
    val moves: Int
    val pushes: Int
    val levelCleared: Boolean

    fun tileAt(position: Position): Tile?
    fun nextTileInDirection(position: Position, direction: Direction?): Tile?

    fun entityAt(position: Position): Entity?
    fun nextEntityInDirection(position: Position, direction: Direction): Entity?

    fun getPlayer(): Entity?
    fun getEntityById(id: String): Entity?
}

abstract class AbstractGameState : GameState {

    override fun tileAt(position: Position): Tile? =
        tiles.getOrNull(position.toIndex(width))

    override fun nextTileInDirection(position: Position, direction: Direction?): Tile? =
        position.nextInDirection(direction!!).run {
            if (x > width || y > height) null
            else tileAt(this)
        }


    override fun entityAt(position: Position): Entity? =
        entities.find { it.position == position }

    override fun nextEntityInDirection(position: Position, direction: Direction): Entity? =
        entityAt(position.nextInDirection(direction))


    override fun getPlayer(): Entity? =
        entities.find(Entity::isPlayer)

    override fun getEntityById(id: String): Entity? =
        entities.find { it.id == id }
}