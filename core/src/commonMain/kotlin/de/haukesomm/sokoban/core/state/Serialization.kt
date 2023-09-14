@file:JvmName("GameStateSerialization")

package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.CharacterMap
import de.haukesomm.sokoban.core.CharacterMaps
import de.haukesomm.sokoban.core.TileProperties
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Converts the given [GameState] to a string representation.
 *
 * The string representation is a layout of the game board. Each tile is represented by a single character. The
 * characters are chosen based on the given [characterMap]. If the [characterMap] does not contain a mapping for a
 * given tile, the character `?` is used instead.
 *
 * The string representation contains line breaks after each row of the game board.
 */
@JvmOverloads
@Suppress("unused")
fun GameState.toLayoutString(characterMap: CharacterMap = CharacterMaps.default): String {
    val characterToTileProperties = characterMap.inverse
    return buildString {
        tiles.forEachIndexed { index, tile ->
            val tileProperties = tile.run {
                TileProperties(type, entity?.type)
            }

            characterToTileProperties[tileProperties]
                ?.let(::append) ?: append('?')

            if ((index + 1) % width == 0)
                append('\n')
        }
    }
}