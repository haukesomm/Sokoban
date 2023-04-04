package de.haukesomm.sokoban.core.level

interface LevelRepository {

    fun getAvailableLevels(): List<LevelDescription>

    fun getLevelOrNull(id: String?): Level?
}
