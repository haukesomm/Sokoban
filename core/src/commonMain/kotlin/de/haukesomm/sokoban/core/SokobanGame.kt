package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.levels.GameStateFromLevelFactory
import de.haukesomm.sokoban.core.levels.LevelRepository
import de.haukesomm.sokoban.core.levels.firstOrThrow
import de.haukesomm.sokoban.core.model.Direction
import de.haukesomm.sokoban.core.model.GameState
import de.haukesomm.sokoban.core.model.LevelDescription
import de.haukesomm.sokoban.core.model.getPlayerPosition
import de.haukesomm.sokoban.core.moving.MoveService
import kotlinx.coroutines.flow.*

/**
 * Represents a Sokoban game.
 *
 * It provides methods to load levels, move entities and check if a level has been cleared.
 * In order to load levels, a [de.haukesomm.sokoban.core.levels.LevelRepository] is required.
 *
 * The current state of the game can be retrieved using the [state]-Flow. A new [de.haukesomm.sokoban.core.model.GameState] object is emitted
 * every time the state of the game changes. The values of the flow can then be collected in order to react
 * to the changes, e.g. in order to update a user interface.
 *
 * New [SokobanGame]s can also be created using the [SokobanGameFactory]. The factory provides a convenient
 * way to create a new game with a [de.haukesomm.sokoban.core.levels.LevelRepository] and a [de.haukesomm.sokoban.core.moving.MoveService] that are already configured.
 * Additionally, a number of configuration options can be specified.
 */
class SokobanGame(
    private val levelRepository: LevelRepository,
    private val moveService: MoveService,
) {
    private val internalState = MutableStateFlow<GameState>(
        GameStateFromLevelFactory.create(levelRepository.firstOrThrow())
    )

    /**
     * Flow emitting each new [GameState] after it has been modified or newly loaded.
     *
     * Use this flow to observe the current state of the game and react to changes.
     */
    val state: Flow<GameState> = internalState.asSharedFlow()

    /**
     * Returns the current [GameState].
     *
     * Do _not_ use this property to observe the state of the game!
     * Instead, use the [state]-Flow to observe changes.
     */
    val currentState: GameState
        get() = internalState.value


    /**
     * Returns the [de.haukesomm.sokoban.core.model.LevelDescription]s of the levels that are available to be loaded.
     */
    fun getAvailableLevels( ): List<LevelDescription> =
        levelRepository.getAvailableLevels()

    /**
     * Loads the level with the given [levelId].
     */
    fun loadLevel(levelId: String) {
        val level = levelRepository.getLevelOrNull(levelId)
            ?: throw IllegalStateException("Level with id '$levelId' does not exist!")

        internalState.tryEmit(GameStateFromLevelFactory.create(level))
    }

    /**
     * Loads the next level if there is one.
     */
    fun loadNextLevelIfAvailable() {
        val currentLevelId = internalState.value.levelId
        levelRepository.getNextLevel(currentLevelId)?.let {
            loadLevel(it.id)
        }
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
                moveService.tryMoveInDirection(currentState, position, direction)
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