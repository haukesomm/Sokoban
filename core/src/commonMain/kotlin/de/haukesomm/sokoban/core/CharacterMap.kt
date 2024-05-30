package de.haukesomm.sokoban.core

/**
 * Convenience Type alias for Character maps
 *
 * A character map is a map that maps [Char]s to [TileProperties], which in turn contain the [TileType] and the
 * [EntityType] of the resulting [TileType] on the game board.
 *
 * All [CharacterMap]s should orient themselves on the default character map, which is defined in the globally
 * available [DefaultCharacterMap].
 */
typealias CharacterMap = BiMap<Char, TileProperties>

/**
 * Default [CharacterMap] as used in the original Sokoban game.
 */
val DefaultCharacterMap: CharacterMap = biMapOf(
    '_' to TileProperties(TileType.Empty),
    '.' to TileProperties(TileType.Target),
    '#' to TileProperties(TileType.Wall),
    '$' to TileProperties(TileType.Empty, EntityType.Box),
    '@' to TileProperties(TileType.Empty, EntityType.Player),
    '*' to TileProperties(TileType.Target, EntityType.Box),
    '+' to TileProperties(TileType.Target, EntityType.Player)
)