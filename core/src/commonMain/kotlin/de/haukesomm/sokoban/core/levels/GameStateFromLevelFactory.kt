package de.haukesomm.sokoban.core.levels

import de.haukesomm.sokoban.core.model.Level
import de.haukesomm.sokoban.core.model.MutableGameState
import de.haukesomm.sokoban.core.model.Tile

/**
 * Factory to create a [GameState][de.haukesomm.sokoban.core.model.GameState] from a [Level].
 */
object GameStateFromLevelFactory {

    /**
     * Returns a [GameState][de.haukesomm.sokoban.core.model.GameState] converted from the given [level].
     */
    fun create(level: Level): MutableGameState =
        with(level) {
            val tiles = normalizedLayoutString
                .map { char -> characterMap.getOrElse(char) { Tile.Ground } }
                .toMutableList()

            MutableGameState(id, width, height, tiles)
        }
}