package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.coroutines.SokobanMainScope
import de.haukesomm.sokoban.core.level.*
import de.haukesomm.sokoban.core.moving.MoveService
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform
import kotlinx.coroutines.flow.*
import kotlin.jvm.JvmOverloads

class GameStateService(
    private val levelRepository: LevelRepository,
    private val levelToGameStateConverter: LevelToGameStateConverter,
    rules: Flow<Collection<MoveRule>> = flowOf(emptyList())
) {
    constructor(
        levelRepository: LevelRepository,
        levelToGameStateConverter: LevelToGameStateConverter,
        vararg rules: MoveRule
    ) : this(levelRepository, levelToGameStateConverter, flowOf(rules.toSet()))


    private val internalState = MutableStateFlow(
        levelToGameStateConverter.convert(levelRepository.firstOrThrow())
    )
    val state: Flow<GameState> = internalState.asSharedFlow()

    private var moveService = MoveService.withDefaultRules()


    init {
        rules.onEach {
            moveService = MoveService.withDefaultRules(additionalRules = it)
        }.launchIn(SokobanMainScope)
    }


    fun getAvailableLevels( ): List<LevelDescription> =
        levelRepository.getAvailableLevels()

    fun loadLevel(levelId: String) {
        val level = levelRepository.getLevelOrNull(levelId)
            ?: throw IllegalStateException("Level with id '$levelId' does not exist!")

        internalState.tryEmit(levelToGameStateConverter.convert(level))
    }

    fun reloadLevel(): Unit =
        loadLevel(internalState.value.levelId)


    fun getPlayerPosition(): Position? =
        internalState.value.getPlayerPosition()


    fun moveEntityIfPossible(position: Position, direction: Direction) {
        moveService.moveEntityIfPossible(internalState.value, position, direction)
            ?.transform { levelCleared = checkLevelCleared(this) }
            ?.let(internalState::tryEmit)
    }

    private fun checkLevelCleared(state: GameState): Boolean =
        state.tiles.none { tile ->
            val box = tile.entity?.takeIf(Entity::isBox)
            tile.isTarget && box == null
        }
}