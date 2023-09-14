@file:JvmName("GameStateSerialization")

package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.CharacterMap
import de.haukesomm.sokoban.core.CharacterMaps
import de.haukesomm.sokoban.core.TileProperties
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

@JvmOverloads
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