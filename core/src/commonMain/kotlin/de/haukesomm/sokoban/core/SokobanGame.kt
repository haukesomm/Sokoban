package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.coroutines.SokobanMainScope
import de.haukesomm.sokoban.core.level.*
import de.haukesomm.sokoban.core.level.bundled.BundledLevelRepository
import de.haukesomm.sokoban.core.moving.MoveService
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
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
    private var moveService: MoveService,
) {
    companion object {

        /**
         * Creates a new [SokobanGame] with a default set of [MoveRule]s replicating the original game's
         * behavior.
         *
         * In case you need to support a dynamically changing set of rules, simply call [updateMoveService] with
         * a new [MoveService] instance that has the desired rules whenever the rules change.
         *
         * The returned [SokobanGame] will be able to play the levels that are bundled with the library.
         */
        @JvmStatic
        fun withBundledLevelsAndRecommendedRules(): SokobanGame =
            SokobanGame(
                BundledLevelRepository(),
                MoveService.withRecommendedRules()
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

    /**
     * Flow emitting `true` if the current state has a previous state, `false` otherwise.
     * This can be used to determine if the user can undo the last move.
     */
    val previousStateExists: Flow<Boolean> = internalState.map { it.previous != null }


    /**
     * Replaces the current [MoveService] used by the game with the given [moveService].
     *
     * This can be used to customize the rules for moving entities.
     */
    fun updateMoveService(moveService: MoveService) {
        this.moveService = moveService
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
            currentState.getPlayerPosition()?.let { position ->
                moveService.moveEntityIfPossible(currentState, position, direction)
                    ?.run(internalState::tryEmit)
            }
        }
    }

    /**
     * Undo the last move if the current state has a previous state.
     *
     * If not, nothing happens.
     */
    fun undoLastMoveIfPossible() {
        internalState.value.previous?.let { prev ->
            internalState.tryEmit(prev)
        }
    }
}