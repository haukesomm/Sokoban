package de.haukesomm.sokoban.legacy

import de.haukesomm.sokoban.core.SokobanGame
import de.haukesomm.sokoban.core.SokobanGameFactory

/**
 * Provides JDK-accessible factory methods mimicking the behavior of the vanilla [SokobanGameFactory].
 */
object JvmSokobanGameFactory {

    /**
     * JDK-accessible version of [SokobanGameFactory.create].
     */
    @JvmStatic
    fun withMinimalConfiguration(): SokobanGame =
        SokobanGameFactory.create()
}