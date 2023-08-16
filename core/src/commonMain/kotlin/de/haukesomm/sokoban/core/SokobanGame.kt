package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.coroutines.SokobanMainScope
import de.haukesomm.sokoban.core.level.*
import de.haukesomm.sokoban.core.level.bundled.BundledLevelRepository
import de.haukesomm.sokoban.core.moving.MoveService
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform
import kotlinx.coroutines.flow.*
import kotlin.jvm.JvmStatic

/**
 * Represents a Sokoban game.
 *
 * It provides methods to load levels, move entities and check if a level has been cleared.
 * In order to load levels, a [LevelRepository] is required.
 *
 * The current state of the game can be retrieved using the [state]-Flow. A new [GameState] object is emitted
 * every time the state of the game changes. The values of the flow can then be collected in order to react
 * to the changes, e.g. in order to update a user interface.
 *
 * A flow of [MoveRule]s can be passed to the service in order to customize the rules for moving entities. If no rules
 * are passed, the service uses a set of default rules.
 */
class SokobanGame(
    private val levelRepository: LevelRepository,
    rules: Flow<Collection<MoveRule>> = flowOf(emptyList())
) {
    companion object {

        /**
         * Creates a new [SokobanGame] with a minimal set of [MoveRule]s replicating the original game's
         * behavior.
         *
         * In case you need to support a dynamically changing set of rules, want to include custom implemented
         * rules or load custom levels, please create an instance of this class manually.
         *
         * The returned [SokobanGame] is able to play levels that are included with the library only.
         */
        @JvmStatic
        fun withDefaultLevelsAndRuleSet(): SokobanGame =
            SokobanGame(
                BundledLevelRepository(),
                rules = flowOf(
                    setOf(
                        WallCollisionPreventingMoveRule(),
                        MultipleBoxesPreventingMoveRule()
                    )
                )
            )
    }


    private val levelToGameStateConverter = LevelToGameStateConverter(levelRepository.characterMap)

    private val internalState = MutableStateFlow(
        levelToGameStateConverter.convert(levelRepository.firstOrThrow())
    )

    /**
     * Flow emitting each new [GameState] after it has been modified or newly loaded.
     *
     * Use this flow to observe the current state of the game and react to changes.
     */
    val state: Flow<GameState> = internalState.asSharedFlow()

    private var moveService = MoveService.withDefaultRules()


    init {
        rules.onEach {
            moveService = MoveService.withDefaultRules(additionalRules = it)
        }.launchIn(SokobanMainScope)
    }


    /**
     * Returns the [LevelDescription]s of the levels that are available to be loaded.
     */
    fun getAvailableLevels( ): List<LevelDescription> =
        levelRepository.getAvailableLevels()

    /**
     * Loads the level with the given [levelId].
     */
    fun loadLevel(levelId: String) {
        val level = levelRepository.getLevelOrNull(levelId)
            ?: throw IllegalStateException("Level with id '$levelId' does not exist!")

        internalState.tryEmit(levelToGameStateConverter.convert(level))
    }

    /**
     * Resets the current level to its initial state.
     */
    fun reloadLevel(): Unit =
        loadLevel(internalState.value.levelId)


    /**
     * Attempts to move the player one step in the given [direction].
     *
     * If the move is not possible or no player is in the level, nothing happens.
     */
    fun movePlayerIfPossible(direction: Direction) {
        internalState.value.let { currentState ->
            currentState.getPlayerPosition()?.let { playerPosition ->
                moveService.moveEntityIfPossible(currentState, playerPosition, direction)
                    ?.transform { levelCleared = checkLevelCleared(this) }
                    ?.let(internalState::tryEmit)
            }
        }
    }

    private fun checkLevelCleared(state: GameState): Boolean =
        state.tiles.none { tile ->
            val box = tile.entity?.takeIf(Entity::isBox)
            tile.isTarget && box == null
        }
}