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

    internal class ConfigurationContext {

        val moveRules = mutableListOf<MoveRule>()
    }

    /**
     * Represents a configuration option for a [SokobanGame].
     *
     * Configuration options are used to customize the behavior of a [SokobanGame] instance.
     * They are used by the [withMinimalConfiguration] method.
     */
    sealed class ConfigurationOption {

        /**
         * The name of the configuration option.
         */
        abstract val name: String

        /**
         * A description of the configuration option.
         */
        abstract val description: String

        internal abstract fun applyTo(config: ConfigurationContext): ConfigurationContext
    }

    /**
     * Configuration option that prevents the user from moving into walls.
     */
    class WallPreventingConfigurationOption : ConfigurationOption() {

        override val name: String = "No walking through walls"

        override val description: String = "Prevents the user from moving into walls."

        override fun applyTo(config: ConfigurationContext): ConfigurationContext =
            config.apply {
                moveRules += WallCollisionPreventingMoveRule()
            }
    }

    /**
     * Configuration option that prevents the user from pushing multiple boxes at once.
     */
    class MultipleBoxesPreventingConfigurationOption : ConfigurationOption() {

        override val name: String = "Push one box at a time"

        override val description: String = "Prevents the user from pushing multiple boxes at once."

        override fun applyTo(config: ConfigurationContext): ConfigurationContext =
            config.apply {
                moveRules += MultipleBoxesPreventingMoveRule()
            }
    }

    /**
     * A list of all available configuration options.
     *
     * The options can be passed to the [withMinimalConfiguration] method in order to customize the
     * behavior of the [SokobanGame] instance.
     */
    val configurationOptions: List<ConfigurationOption> = listOf(
        WallPreventingConfigurationOption(),
        MultipleBoxesPreventingConfigurationOption()
    )

    /**
     * Creates a new [SokobanGame] with builtin levels and a minimal set of rules.
     *
     * These include basic necessary rules to prevent the user from moving entities out of the game board,
     * prevent the user from moving entities after the game has been completed and enables the user to push
     * boxes.
     *
     * Additional behavior can be added by passing additional [ConfigurationOption]s. A list of available
     * options can be found in [configurationOptions].
     */
    @JvmStatic
    @JvmOverloads
    fun withMinimalConfiguration(additional: Collection<ConfigurationOption> = emptySet()): SokobanGame {
        val combinedConfig = additional.fold(ConfigurationContext()) { scope, config ->
            config.applyTo(scope)
        }
        return SokobanGame(
            PaddingLevelRepositoryDecorator(
                BundledLevelRepository(),
                minWidth = 20,
                minHeight = 16
            ),
            MoveServiceImpl(
                ConditionalMoveRule(
                    condition = AggregatingMoveRule(
                        OutOfBoundsPreventingMoveRule(),
                        MoveAfterCompletionPreventingMoveRule()
                    ),
                    moveRules = setOf(
                        BoxDetectingMoveRule(),
                        *combinedConfig.moveRules.toTypedArray()
                    )
                )
            )
        )
    }

    /**
     * Creates a new [SokobanGame] that uses builtin levels and a default set of rules.
     *
     * The default configuration includes the configuration options defined in [configurationOptions].
     *
     * @see withMinimalConfiguration
     */
    @JvmStatic
    fun withDefaultConfiguration(): SokobanGame =
        withMinimalConfiguration(
            additional = configurationOptions
        )
}