package de.haukesomm.sokoban.core

/**
 * Converts a [Level] into a [GameState] by using a [CharacterMap] to map the characters of the level's layout string
 * to a two-dimensional grid of [Tile]s.
 */
internal class LevelParser(private val characterMap: CharacterMap) {

    private fun tileForCharacter(character: Char): Tile =
        characterMap
            .getOrElse(character) { TileProperties(TileType.Empty) }
            .run(Tile::fromTileProperties)

    fun convert(level: Level): GameState =
        level.run {
            val tiles = normalizedLayoutString.map(::tileForCharacter)
            ImmutableGameState(id, width, height, tiles)
        }
}