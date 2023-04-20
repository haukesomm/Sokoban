package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.ImmutableGameState

class LevelToGameStateConverter(private val tileFactory: TileFactory) {

    fun convert(level: Level): GameState {
        val layoutCharacters = level.layoutString.toCharArray()
        val tiles = mutableListOf<Tile>()

        for (y in 0 until level.height) {
            for (x in 0 until level.width) {
                val position = Position(x, y)
                val character = layoutCharacters[position.toIndex(level.width)]
                tiles += tileFactory.createForCharacter(character, position)
            }
        }

        return ImmutableGameState(level.id, level.width, level.height, tiles)
    }
}