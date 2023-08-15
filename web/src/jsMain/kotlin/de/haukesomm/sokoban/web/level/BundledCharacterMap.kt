package de.haukesomm.sokoban.web.level

import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.TileType
import de.haukesomm.sokoban.core.level.*

@Suppress("UnusedReceiverParameter")
val CharacterMaps.bundled: CharacterMap
    get() = characterMapOf(
        '_' to TileProperties(TileType.Empty),
        '.' to TileProperties(TileType.Target),
        '#' to TileProperties(TileType.Wall),
        'X' to TileProperties(TileType.Empty, EntityType.Box),
        '@' to TileProperties(TileType.Empty, EntityType.Player),
    )