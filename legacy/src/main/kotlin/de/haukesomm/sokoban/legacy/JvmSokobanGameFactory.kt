package de.haukesomm.sokoban.legacy

import de.haukesomm.sokoban.core.LevelRepository
import de.haukesomm.sokoban.core.SokobanGame
import de.haukesomm.sokoban.core.SokobanGameFactory
import de.haukesomm.sokoban.core.levels.BundledLevelRepository

/**
 * Provides JDK-accessible factory methods mimicking the behavior of the vanilla [SokobanGameFactory].
 */
object JvmSokobanGameFactory {

    /**
     * JDK-accessible version of [SokobanGameFactory.withDefaultConfiguration].
     */
    @JvmStatic
    @JvmOverloads
    fun withDefaultConfiguration(levelRepository: LevelRepository = BundledLevelRepository()): SokobanGame =
        SokobanGameFactory.withMinimalConfiguration(levelRepository)
}