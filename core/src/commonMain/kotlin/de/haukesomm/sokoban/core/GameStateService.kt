package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.level.LevelDescription
import de.haukesomm.sokoban.core.level.LevelRepository
import de.haukesomm.sokoban.core.level.LevelToGameStateConverter
import de.haukesomm.sokoban.core.level.SokobanLevelCharacterMap
import de.haukesomm.sokoban.core.moving.MoveCoordinatorFactory
import de.haukesomm.sokoban.core.moving.validation.MoveValidatorStatus

class GameStateService(private val levelRepository: LevelRepository) {

    interface StateChangeListener {
        fun onGameStateChange(state: GameState, moves: Int, pushes: Int, levelCleared: Boolean)
    }


    private val levelToGameStateConverter = LevelToGameStateConverter(SokobanLevelCharacterMap())

    private val moveCoordinator = MoveCoordinatorFactory.newWithDefaultValidators()

    private val stateChangeListeners = mutableSetOf<StateChangeListener>()


    private lateinit var state: GameState

    private var moves = 0

    private var pushes = 0

    private var levelCleared = false


    fun addGameStateChangedListener(listener: StateChangeListener) {
        stateChangeListeners += listener
    }

    private fun notifyGameStateChangedListeners() {
        stateChangeListeners.forEach { it.onGameStateChange(state, moves, pushes, levelCleared) }
    }


    fun getAvailableLevels(): List<LevelDescription> = levelRepository.getAvailableLevels()

    fun loadLevel(levelId: String) {
        val level = levelRepository.getLevelOrNull(levelId)
            ?: throw IllegalStateException("Level with id '$levelId' does not exist!")

        moves = 0
        pushes = 0
        levelCleared = false

        state = levelToGameStateConverter.convert(level)
        notifyGameStateChangedListeners()
    }


    fun getPlayer(): Entity? = state.getPlayer()


    fun moveEntityIfPossible(entity: Entity, direction: Direction) {
        if (!this::state.isInitialized) {
            throw IllegalStateException("Cannot move Entity: No game state loaded!")
        }

        val moveCoordinatorResult = moveCoordinator.moveEntityIfPossible(state, entity, direction)
        if (moveCoordinatorResult.success) {
            moves++
            // TODO: Create getter method for this:
            if (MoveValidatorStatus.BOX_AHEAD_NEEDS_TO_MOVE in moveCoordinatorResult.moveValidatorStatuses) {
                pushes++
            }
        }

        moveCoordinatorResult.gameState?.let { newState ->
            levelCleared = checkLevelCleared(newState)
            state = newState
        }

        notifyGameStateChangedListeners()
    }

    private fun checkLevelCleared(state: GameState): Boolean {
        state.tiles.forEachIndexed { y, row ->
            row.forEachIndexed { x, tile ->
                val entity = state.getEntityAtPositionOrNull(Position(x, y))
                if (tile.type == TileType.TARGET && (entity == null || entity.type != EntityType.BOX)) {
                    return false
                }
            }
        }
        return true
    }
}