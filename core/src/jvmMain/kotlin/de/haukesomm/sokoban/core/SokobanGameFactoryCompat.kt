package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.levels.BundledLevelRepository

/**
 * Provides JDK-accessible factory methods mimicking the behavior of the vanilla [SokobanGameFactory].
 */
object SokobanGameFactoryCompat {

    /**
     * JDK-accessible version of [SokobanGameFactory.withDefaultConfiguration].
     */
    @JvmStatic
    @JvmOverloads
    fun withDefaultConfiguration(levelRepository: LevelRepository = BundledLevelRepository()): SokobanGame =
        SokobanGameFactory.withMinimalConfiguration(levelRepository)
}