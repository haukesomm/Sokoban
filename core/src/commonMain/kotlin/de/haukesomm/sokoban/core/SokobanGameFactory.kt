package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.levels.BundledLevelRepository
import de.haukesomm.sokoban.core.levels.PaddingLevelRepositoryDecorator
import de.haukesomm.sokoban.core.moving.MoveRule
import de.haukesomm.sokoban.core.moving.MoveServiceImpl
import de.haukesomm.sokoban.core.moving.rules.*
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Factory for creating [SokobanGame] instances.
 */
object SokobanGameFactory {

    /**
     * Creates a new [SokobanGame] with builtin levels and a minimal set of rules.
     *
     * These include basic necessary rules to prevent the user from moving entities out of the game board,
     * prevent the user from moving entities after the game has been completed and enables the user to push
     * boxes.
     *
     * The behavior can be customized via the respective arguments:
     *
     * | Argument          | Description                                                             |
     * |-------------------|-------------------------------------------------------------------------|
     * | [levelRepository] | Specify a [LevelRepository] to use. Bundled levels are used by default. |
     * | [additionalRules] | Additional [MoveRule]s to use.                                          |
     *
     * Use [basedOnConfiguration] in order to set up the game based on predefined
     * [ConfigurationOption]s.
     */
    fun withMinimalConfiguration(
        levelRepository: LevelRepository = BundledLevelRepository(),
        additionalRules: Collection<MoveRule> = emptySet()
    ): SokobanGame =
        SokobanGame(
            levelRepository = PaddingLevelRepositoryDecorator(
                levelRepository,
                minWidth = 20,
                minHeight = 16
            ),
            moveService = MoveServiceImpl(
                ConditionalMoveRule(
                    condition = AggregatingMoveRule(
                        OutOfBoundsPreventingMoveRule(),
                        MoveAfterCompletionPreventingMoveRule()
                    ),
                    moveRules = setOf(
                        BoxDetectingMoveRule(),
                        *additionalRules.toTypedArray()
                    )
                )
            )
        )

    /**
     * Creates a new [SokobanGame] that uses builtin levels and includes all move rules needed to
     * mimic the original Sokoban game.
     *
     * @see withMinimalConfiguration
     */
    fun withDefaultConfiguration(levelRepository: LevelRepository = BundledLevelRepository()): SokobanGame =
        withMinimalConfiguration(
            levelRepository = levelRepository,
            additionalRules = setOf(
                WallCollisionPreventingMoveRule(),
                MultipleBoxesPreventingMoveRule()
            )
        )
}