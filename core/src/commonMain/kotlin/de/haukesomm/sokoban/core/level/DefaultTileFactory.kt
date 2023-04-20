package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType

class DefaultTileFactory : TileFactory(
    '_' to BasicTileProvider(TileType.NOTHING),
    '.' to BasicTileProvider(TileType.TARGET),
    '#' to BasicTileProvider(TileType.WALL),
    '$' to EntityTileProvider(EntityType.BOX),
    '@' to EntityTileProvider(EntityType.PLAYER),
    default = BasicTileProvider(TileType.NOTHING)
)