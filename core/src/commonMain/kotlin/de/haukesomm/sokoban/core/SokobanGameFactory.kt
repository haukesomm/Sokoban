package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.levels.BundledLevelRepository
import de.haukesomm.sokoban.core.levels.PaddingLevelRepositoryDecorator
import de.haukesomm.sokoban.core.moving.StateMachineBasedBasedMoveService

/**
 * Factory for creating [SokobanGame] instances.
 */
object SokobanGameFactory {

    /**
     * Creates a new [SokobanGame] with builtin levels.
     */
    fun create(): SokobanGame =
        SokobanGame(
            levelRepository = PaddingLevelRepositoryDecorator(
                BundledLevelRepository(),
                minWidth = 20,
                minHeight = 16
            ),
            moveService = StateMachineBasedBasedMoveService()
        )
}