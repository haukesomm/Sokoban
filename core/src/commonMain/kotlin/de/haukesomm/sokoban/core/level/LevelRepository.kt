package de.haukesomm.sokoban.core.level

import kotlin.jvm.JvmField

interface LevelRepository {

    fun getAvailableLevels(): List<LevelDescription>

    fun getLevelOrNull(id: String?): Level?
}
