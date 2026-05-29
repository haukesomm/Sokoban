package de.haukesomm.sokoban.core.model

/**
 * Convenience Type alias for Character maps
 *
 * A character map is a map that maps [Char]s to [Tile]s.
 *
 * All [CharacterMap]s should orient themselves on the default character map, which is defined in the globally
 * available [DefaultCharacterMap].
 */
typealias CharacterMap = BiMap<Char, Tile>

/**
 * Default [CharacterMap] as used in the original Sokoban game.
 */
val DefaultCharacterMap: CharacterMap = biMapOf(
    '_' to Tile.Ground,
    '.' to Tile.Target,
    '#' to Tile.Wall,
    '$' to Tile.BoxOnGround,
    '*' to Tile.BoxOnTarget,
    '@' to Tile.PlayerOnGround,
    '+' to Tile.PlayerOnTarget
)