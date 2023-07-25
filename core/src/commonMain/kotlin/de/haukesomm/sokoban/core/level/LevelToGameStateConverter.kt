package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.ImmutableGameState
import kotlin.jvm.JvmStatic

data class TileProperties(
    val tileType: TileType,
    val entityType: EntityType? = null
)

class LevelToGameStateConverter(
    vararg tilePropertiesToCharacter: Pair<Char, TileProperties>
) {
    private val tilePropertiesToCharacterMap = mapOf(*tilePropertiesToCharacter)

    private fun tileForCharacter(character: Char): Tile {
        val mapValue = tilePropertiesToCharacterMap[character]
            ?: TileProperties(TileType.Empty)

        return mapValue.run {
            Tile(tileType, entityType?.let { Entity(it) })
        }
    }

    fun convert(level: Level): GameState =
        level.run {
            ImmutableGameState(id, width, height, layoutString.map(::tileForCharacter))
        }

    companion object {
        @JvmStatic
        val default: LevelToGameStateConverter = LevelToGameStateConverter(
            '_' to TileProperties(TileType.Empty),
            '.' to TileProperties(TileType.Target),
            '#' to TileProperties(TileType.Wall),
            '$' to TileProperties(TileType.Empty, EntityType.Box),
            '@' to TileProperties(TileType.Empty, EntityType.Player),
        )
    }
}