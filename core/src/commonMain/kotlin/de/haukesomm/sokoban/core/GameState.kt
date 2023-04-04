package de.haukesomm.sokoban.core

data class GameState(
    val levelId: String,
    val tiles: Array<Array<Tile>>,
    val entities: Collection<Entity>,
    val moves: Int = 0,
    val pushes: Int = 0,
    val levelCleared: Boolean = false
) {
    val width: Int = tiles[0].size

    val height: Int = tiles.size


    fun tileAt(x: Int, y: Int): Tile = tiles[y][x]

    fun getNextTileInDirectionOrNull(position: Position, direction: Direction?): Tile? {
        val (x, y) = position.nextInDirection(direction!!)
        return if (y > tiles.size || tiles.isNotEmpty() && x > tiles[0].size) {
            null
        } else tiles[y][x]
    }


    fun entityAt(x: Int, y: Int): Entity? {
        val position = Position(x, y)
        return entities.find { it.position == position }
    }

    fun entityAt(position: Position): Entity? = entityAt(position.x, position.y)

    fun getEntityAtNextPositionOrNull(position: Position, direction: Direction): Entity? =
        entityAt(position.nextInDirection(direction))


    fun getPlayer(): Entity? = entities.find { it.type == EntityType.PLAYER }

    fun getEntityById(id: String): Entity? = entities.find { it.id == id }

    companion object
}
