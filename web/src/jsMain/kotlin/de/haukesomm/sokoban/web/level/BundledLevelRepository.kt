package de.haukesomm.sokoban.web.level

import de.haukesomm.sokoban.core.level.Level
import de.haukesomm.sokoban.core.level.LevelDescription
import de.haukesomm.sokoban.core.level.LevelRepository

class BundledLevelRepository : LevelRepository {
    override fun getAvailableLevels(): List<LevelDescription> =
        BundledLevelDefinitions.levels.map(Level::toLevelDescription)

    override fun getLevelOrNull(id: String?): Level? =
        BundledLevelDefinitions.levelById[id]
}