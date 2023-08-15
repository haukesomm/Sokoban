package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.ImmutableGameState

/**
 * Represents both the type of tile and the type of entity that may be present on the tile.
 * The latter is optional and may be `null` if the tile does not contain an entity.
 */
data class TileProperties(
    val tileType: TileType,
    val entityType: EntityType? = null
)

/**
 * Converts a [Level] into a [GameState] by using a [CharacterMap] to map the characters of the level's layout string
 * to a two-dimensional grid of [Tile]s.
 */
class LevelToGameStateConverter(private val characterMap: CharacterMap) {

    private fun tileForCharacter(character: Char): Tile {
        val tileProperties = characterMap[character]
            ?: TileProperties(TileType.Empty)

        return tileProperties.run {
            Tile(tileType, entityType?.let { Entity(it) })
        }
    }

    /**
     * Converts the given [level] into a [GameState].
     */
    fun convert(level: Level): GameState =
        level.run {
            ImmutableGameState(
                id,
                width,
                height,
                normalizedLayoutString.map(::tileForCharacter)
            )
        }
}