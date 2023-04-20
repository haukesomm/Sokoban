package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.*

fun interface TileProvider {
    fun create(position: Position): Tile
}

class BasicTileProvider(private val tileType: TileType) : TileProvider {
    override fun create(position: Position): Tile =
        Tile(tileType, position)
}

class EntityTileProvider(private val entityType: EntityType) : TileProvider {
    override fun create(position: Position): Tile {
        val entity = Entity(type = entityType, position = position)
        return Tile(TileType.NOTHING, position, entities = setOf(entity))
    }
}

open class TileFactory(
    vararg providerToCharacterPairs: Pair<Char, TileProvider>,
    private val default: TileProvider
) {
    private val mapping = mapOf(*providerToCharacterPairs)

    fun createForCharacter(character: Char, position: Position): Tile =
        mapping[character]?.create(position) ?: default.create(position)
}