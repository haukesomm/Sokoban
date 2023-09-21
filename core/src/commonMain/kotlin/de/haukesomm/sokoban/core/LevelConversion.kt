package de.haukesomm.sokoban.core

/**
 * Converts the given [Level] to a [GameState].
 */
fun Level.toGameState(): GameState {
    val tiles = normalizedLayoutString.map { character ->
        characterMap
            .getOrElse(character) { TileProperties(TileType.Empty) }
            .run(Tile::fromTileProperties)
    }
    return ImmutableGameState(id, width, height, tiles)
}