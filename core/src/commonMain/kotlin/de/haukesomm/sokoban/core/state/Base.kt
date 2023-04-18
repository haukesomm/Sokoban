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

    fun tileAt(x: Int, y: Int): Tile?
    fun tileAt(position: Position): Tile?
    fun getNextTileInDirectionOrNull(position: Position, direction: Direction?): Tile?

    fun entityAt(x: Int, y: Int): Entity?
    fun entityAt(position: Position): Entity?
    fun getEntityAtNextPositionOrNull(position: Position, direction: Direction): Entity?

    fun getPlayer(): Entity?
    fun getEntityById(id: String): Entity?
}

abstract class AbstractGameState : GameState {

    override fun tileAt(x: Int, y: Int): Tile? = tiles
        .getOrNull(Position(x, y).toIndex(width))

    override fun tileAt(position: Position): Tile? =
        position.run { tileAt(x, y) }

    override fun getNextTileInDirectionOrNull(position: Position, direction: Direction?): Tile? {
        val (x, y) = position.nextInDirection(direction!!)

        return if (x > width || y > height) null
        else tileAt(x, y)
    }


    override fun entityAt(x: Int, y: Int): Entity? = entityAt(Position(x, y))

    override fun entityAt(position: Position): Entity? = entities.find { it.position == position }

    override fun getEntityAtNextPositionOrNull(position: Position, direction: Direction): Entity? =
        entityAt(position.nextInDirection(direction))


    override fun getPlayer(): Entity? =
        entities.find(Entity::isPlayer)

    override fun getEntityById(id: String): Entity? =
        entities.find { it.id == id }
}