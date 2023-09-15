package de.haukesomm.sokoban.core.levels

import de.haukesomm.sokoban.core.CharacterMap
import de.haukesomm.sokoban.core.Level
import de.haukesomm.sokoban.core.LevelDescription
import de.haukesomm.sokoban.core.LevelRepository

/**
 * [LevelRepository] implementation that contains the levels bundled with the game.
 */
class BundledLevelRepository : LevelRepository {

    override val characterMap: CharacterMap =
        BundledLevels.characterMap

    override fun getAvailableLevels(): List<LevelDescription> =
        BundledLevels.levels.map { LevelDescription(it.id, it.name) }

    override fun getLevelOrNull(id: String?): Level? =
        BundledLevels.levelById[id]
}