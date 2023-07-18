package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.level.*
import de.haukesomm.sokoban.core.moving.MoveCoordinator
import de.haukesomm.sokoban.core.moving.MoveCoordinatorFactory
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.moving.rules.ConditionalMoveRule
import de.haukesomm.sokoban.core.moving.rules.OutOfBoundsPreventingMoveRule
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform
import kotlin.jvm.JvmOverloads

class GameStateService(
    private val levelRepository: LevelRepository,
    tileFactory: TileFactory
) {
    fun interface StateChangeListener {
        fun onGameStateChange(state: GameState)
    }


    private val levelToGameStateConverter = LevelToGameStateConverter(tileFactory)

    private val stateChangeListeners = mutableSetOf<StateChangeListener>()


    private lateinit var state: GameState

    private var moveCoordinator = MoveCoordinatorFactory.create()


    fun addGameStateChangedListener(listener: StateChangeListener) {
        stateChangeListeners += listener
    }

    private fun notifyGameStateChangedListeners() {
        stateChangeListeners.forEach { it.onGameStateChange(state) }
    }


    fun setCustomRules(rules: Collection<MoveRule>) {
        moveCoordinator = MoveCoordinatorFactory.create(rules)
    }


    fun getAvailableLevels(): List<LevelDescription> =
        levelRepository.getAvailableLevels()

    fun loadLevel(levelId: String) {
        val level = levelRepository.getLevelOrNull(levelId)
            ?: throw IllegalStateException("Level with id '$levelId' does not exist!")

        state = levelToGameStateConverter.convert(level)
        notifyGameStateChangedListeners()
    }

    fun reloadLevel() = loadLevel(state.levelId)


    fun getPlayer(): Entity? = state.getPlayer()


    fun moveEntityIfPossible(entity: Entity, direction: Direction) {
        if (!this::state.isInitialized) {
            throw IllegalStateException("Cannot move Entity: No game state loaded!")
        }

        moveCoordinator.moveEntityIfPossible(state, entity, direction)?.let { newState ->
            state = newState.transform {
                levelCleared = checkLevelCleared(newState)
            }
        }

        notifyGameStateChangedListeners()
    }


    private fun checkLevelCleared(state: GameState): Boolean =
        state.tiles.none { tile ->
            val box = state.entityAt(tile.position)?.takeIf(Entity::isBox)
            tile.isTarget && box == null
        }
}