package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.coroutines.SokobanMainScope
import de.haukesomm.sokoban.core.level.*
import de.haukesomm.sokoban.core.moving.MoveService
import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform
import kotlinx.coroutines.flow.*
import kotlin.jvm.JvmOverloads

/**
 * Service that manages a [GameState].
 *
 * It provides methods to load levels, move entities and check if a level has been cleared.
 * In order to load levels, a [LevelRepository] is required. A [CharacterMap] is required as well in order to
 * convert a [Level] to a [GameState].
 *
 * A flow of [MoveRule]s can be passed to the service in order to customize the rules for moving entities. If no rules
 * are passed, the service uses a set of default rules.
 */
class GameStateService(
    private val levelRepository: LevelRepository,
    characterMap: CharacterMap,
    rules: Flow<Collection<MoveRule>> = flowOf(emptyList())
) {
    constructor(
        levelRepository: LevelRepository,
        characterMap: CharacterMap,
        vararg rules: MoveRule
    ) : this(levelRepository, characterMap, flowOf(rules.toSet()))


    private val levelToGameStateConverter = LevelToGameStateConverter(characterMap)

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
     * Returns the current [Position] of the player or `null` if the player is not on the game board.
     */
    fun getPlayerPosition(): Position? =
        internalState.value.getPlayerPosition()


    /**
     * Moves the entity at the given [position] in the given [direction] if possible.
     *
     * If the move is not possible, nothing happens.
     */
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