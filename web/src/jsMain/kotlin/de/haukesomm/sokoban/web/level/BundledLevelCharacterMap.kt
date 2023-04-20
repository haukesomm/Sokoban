package de.haukesomm.sokoban.web.level

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType
import de.haukesomm.sokoban.core.level.BasicTileProvider
import de.haukesomm.sokoban.core.level.EntityTileProvider
import de.haukesomm.sokoban.core.level.TileFactory

class BundledLevelTileFactory : TileFactory(
    '_' to BasicTileProvider(TileType.NOTHING),
    '.' to BasicTileProvider(TileType.TARGET),
    '#' to BasicTileProvider(TileType.WALL),
    'X' to EntityTileProvider(EntityType.BOX),
    '@' to EntityTileProvider(EntityType.PLAYER),
    default = BasicTileProvider(TileType.NOTHING)
)