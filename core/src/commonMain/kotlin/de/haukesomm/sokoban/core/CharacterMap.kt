package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.state.GameState
import kotlin.jvm.JvmStatic

/**
 * This class represents a character map.
 *
 * A character map is a map that maps [Char]s to [TileProperties], which in turn contain the [TileType] and the
 * [EntityType] of the resulting [TileType] on the game board.
 *
 * It is used to convert a [Level] (which contains the level's layout in a String-encoded form)
 * to a [GameState] using the [LevelToGameStateConverter].
 */
typealias CharacterMap = BiMap<Char, TileProperties>

/**
 * Creates a [CharacterMap] from the given [entries].
 */
fun characterMapOf(vararg entries: Pair<Char, TileProperties>): CharacterMap =
    BiMap(mapOf(*entries))

/**
 * Contains various predefined [CharacterMap]s.
 *
 * Custom defined character maps can be registered as an extension property of this object.
 * They can be created using the [characterMapOf] function.
 *
 * @see characterMapOf
 */
object CharacterMaps {

    /**
     * Default character map based on the original Sokoban game.
     */
    @JvmStatic
    val default: CharacterMap = characterMapOf(
        '_' to TileProperties(TileType.Empty),
        '.' to TileProperties(TileType.Target),
        '#' to TileProperties(TileType.Wall),
        '$' to TileProperties(TileType.Empty, EntityType.Box),
        '@' to TileProperties(TileType.Empty, EntityType.Player),
    )
}