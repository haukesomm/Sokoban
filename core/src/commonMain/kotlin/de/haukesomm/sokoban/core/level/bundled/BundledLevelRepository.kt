package de.haukesomm.sokoban.core.level.bundled

import de.haukesomm.sokoban.core.CharacterMap
import de.haukesomm.sokoban.core.level.*

class BundledLevelRepository : LevelRepository {

    override val characterMap: CharacterMap =
        bundledCharacterMap

    override fun getAvailableLevels(): List<LevelDescription> =
        bundledLevels.map { LevelDescription(it.id, it.name) }

    override fun getLevelOrNull(id: String?): Level? =
        bundledLevelById[id]
}