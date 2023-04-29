package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Tile

class DefaultTileFactory : TileFactory(
    '_' to BasicTileProvider(Tile.Type.Empty),
    '.' to BasicTileProvider(Tile.Type.Target),
    '#' to BasicTileProvider(Tile.Type.Wall),
    '$' to EntityTileProvider(Entity.Type.Box),
    '@' to EntityTileProvider(Entity.Type.Player),
    default = BasicTileProvider(Tile.Type.Empty)
)