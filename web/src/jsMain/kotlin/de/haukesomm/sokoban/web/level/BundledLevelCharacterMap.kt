package de.haukesomm.sokoban.web.level

import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Tile
import de.haukesomm.sokoban.core.level.BasicTileProvider
import de.haukesomm.sokoban.core.level.EntityTileProvider
import de.haukesomm.sokoban.core.level.TileFactory

class BundledLevelTileFactory : TileFactory(
    '_' to BasicTileProvider(Tile.Type.Empty),
    '.' to BasicTileProvider(Tile.Type.Target),
    '#' to BasicTileProvider(Tile.Type.Wall),
    'X' to EntityTileProvider(Entity.Type.Box),
    '@' to EntityTileProvider(Entity.Type.Player),
    default = BasicTileProvider(Tile.Type.Empty)
)