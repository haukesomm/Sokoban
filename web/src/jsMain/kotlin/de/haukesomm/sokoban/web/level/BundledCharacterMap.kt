package de.haukesomm.sokoban.web.level

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType
import de.haukesomm.sokoban.core.level.LevelToGameStateConverter
import de.haukesomm.sokoban.core.level.TileProperties

val bundledTileMapping: LevelToGameStateConverter = LevelToGameStateConverter(
    '_' to TileProperties(TileType.Empty),
    '.' to TileProperties(TileType.Target),
    '#' to TileProperties(TileType.Wall),
    'X' to TileProperties(TileType.Empty, EntityType.Box),
    '@' to TileProperties(TileType.Empty, EntityType.Player),
)