package de.haukesomm.sokoban.web.level

import de.haukesomm.sokoban.core.level.Level
import de.haukesomm.sokoban.core.level.LevelDescription
import de.haukesomm.sokoban.core.level.LevelRepository

class BundledLevelRepository : LevelRepository {
    override fun getAvailableLevels(): List<LevelDescription> =
        BundledLevelDefinitions.levels.map { LevelDescription(it.id, it.name) }

    override fun getLevelOrNull(id: String?): Level? =
        BundledLevelDefinitions.levelById[id]
}