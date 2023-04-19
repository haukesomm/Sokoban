package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.ImmutableGameState

class LevelToGameStateConverter(private val levelCharacterMap: LevelCharacterMap) {

    fun convert(level: Level): GameState {
        val layoutCharacters = level.layoutString.toCharArray()
        val tiles = mutableListOf<Tile>()

        (0 until level.height).forEach { y ->
            (0 until level.width).forEach { x ->
                val position = Position(x, y)
                val character = layoutCharacters[position.toIndex(level.width)]
                val entities = mutableSetOf<Entity>()

                levelCharacterMap.getEntityType(character)?.let { type ->
                    entities.add(Entity(type = type, position = position))
                }

                tiles.add(Tile(levelCharacterMap.getTileType(character), position, entities))
            }
        }

        return ImmutableGameState(level.id, level.width, level.height, tiles)
    }
}