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

class GameStateService @JvmOverloads constructor(
    private val levelRepository: LevelRepository,
    tileFactory: TileFactory,
    customRules: Set<MoveRule>? = null
) {
    fun interface StateChangeListener {
        fun onGameStateChange(state: GameState)
    }


    private val levelToGameStateConverter = LevelToGameStateConverter(tileFactory)

    private var moveCoordinator = customRules
        ?.let { MoveCoordinatorFactory.withMinimalRecommendedRules(additional = it) }
        ?: MoveCoordinatorFactory.withDefaultRules()

    private val stateChangeListeners = mutableSetOf<StateChangeListener>()


    private lateinit var state: GameState


    fun addGameStateChangedListener(listener: StateChangeListener) {
        stateChangeListeners += listener
    }

    private fun notifyGameStateChangedListeners() {
        stateChangeListeners.forEach { it.onGameStateChange(state) }
    }


    /**
     * Overrides the standard recommended rules with a set of custom rules.
     *
     * A minimal set of rules will always be enabled, regardless of the given [rules]:
     * - Enable movement of boxes
     * - Prevent moves outside the game field
     */
    fun setCustomRules(rules: Set<MoveRule>) {
        moveCoordinator = MoveCoordinatorFactory.withMinimalRecommendedRules(additional = rules)
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