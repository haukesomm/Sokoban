package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.CharacterMap.Companion.default
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmStatic

/**
 * This class represents a character map.
 *
 * A character map is a map that maps [Char]s to [TileProperties], which in turn contain the [TileType] and the
 * [EntityType] of the resulting [TileType] on the game board.
 *
 * All [CharacterMap]s should orient themselves on the default character map, which is defined in the companion object's
 * [default] property.
 */
class CharacterMap(map: BiMap<Char, TileProperties>) : BiMap<Char, TileProperties> by map {

    companion object {

        /**
         * Default character map based on the original Sokoban game.
         */
        val default: CharacterMap = characterMapOf(
            '_' to TileProperties(TileType.Empty),
            '.' to TileProperties(TileType.Target),
            '#' to TileProperties(TileType.Wall),
            '$' to TileProperties(TileType.Empty, EntityType.Box),
            '@' to TileProperties(TileType.Empty, EntityType.Player),
            '*' to TileProperties(TileType.Target, EntityType.Box),
            '+' to TileProperties(TileType.Target, EntityType.Player)
        )
    }
}

/**
 * Creates a [CharacterMap] from the given [entries].
 */
fun characterMapOf(vararg entries: Pair<Char, TileProperties>): CharacterMap =
    CharacterMap(
        BiMapImpl(
            mapOf(*entries)
        )
    )