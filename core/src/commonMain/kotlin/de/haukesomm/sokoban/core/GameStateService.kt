package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.level.*
import de.haukesomm.sokoban.core.moving.MoveCoordinatorFactory
import de.haukesomm.sokoban.core.moving.validation.MoveValidatorStatus

class GameStateService(
    private val levelRepository: LevelRepository,
    levelCharacterMap: LevelCharacterMap
) {

    fun interface StateChangeListener {
        fun onGameStateChange(state: GameState)
    }


    private val levelToGameStateConverter = LevelToGameStateConverter(levelCharacterMap)

    private val moveCoordinator = MoveCoordinatorFactory.newWithDefaultValidators()

    private val stateChangeListeners = mutableSetOf<StateChangeListener>()


    private lateinit var state: GameState


    fun addGameStateChangedListener(listener: StateChangeListener) {
        stateChangeListeners += listener
    }

    private fun notifyGameStateChangedListeners() {
        stateChangeListeners.forEach { it.onGameStateChange(state) }
    }


    fun getAvailableLevels(): List<LevelDescription> = levelRepository.getAvailableLevels()

    fun loadLevel(levelId: String) {
        val level = levelRepository.getLevelOrNull(levelId)
            ?: throw IllegalStateException("Level with id '$levelId' does not exist!")

        state = levelToGameStateConverter.convert(level)
        notifyGameStateChangedListeners()
    }

    fun reload() = loadLevel(state.levelId)


    fun getPlayer(): Entity? = state.getPlayer()


    fun moveEntityIfPossible(entity: Entity, direction: Direction) {
        if (!this::state.isInitialized) {
            throw IllegalStateException("Cannot move Entity: No game state loaded!")
        }

        moveCoordinator.moveEntityIfPossible(state, entity, direction).gameState?.let { newState ->
            state = newState.copy(levelCleared = checkLevelCleared(newState))
        }

        notifyGameStateChangedListeners()
    }

    private fun checkLevelCleared(state: GameState): Boolean {
        state.tiles.forEachIndexed { y, row ->
            row.forEachIndexed { x, tile ->
                val entity = state.entityAt(Position(x, y))
                if (tile.type == TileType.TARGET && (entity == null || entity.type != EntityType.BOX)) {
                    return false
                }
            }
        }
        return true
    }
}