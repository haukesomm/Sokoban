package de.haukesomm.sokoban.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

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
 * New [SokobanGame]s can also be created using the [SokobanGameFactory]. The factory provides a convenient
 * way to create a new game with a [LevelRepository] and a [MoveService] that are already configured.
 * Additionally, a number of configuration options can be specified.
 */
class SokobanGame(
    private val levelRepository: LevelRepository,
    private val moveService: MoveService,
) {
    private val internalState = MutableStateFlow(
        levelRepository.firstOrThrow().toGameState()
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

        internalState.tryEmit(level.toGameState())
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