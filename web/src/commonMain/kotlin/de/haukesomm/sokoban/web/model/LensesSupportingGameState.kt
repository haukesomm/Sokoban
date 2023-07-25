package de.haukesomm.sokoban.web.model

import de.haukesomm.sokoban.core.Tile
import de.haukesomm.sokoban.core.state.GameState
import dev.fritz2.core.Lenses
import kotlinx.serialization.Serializable

/**
 * A [GameState] that supports [Lenses] for its properties and is serializable.
 */
@Lenses
@Serializable
data class LensesSupportingGameState(
    override val levelId: String,
    override val width: Int,
    override val height: Int,
    override val tiles: List<Tile>,
    override val moves: Int = 0,
    override val pushes: Int = 0,
    override val levelCleared: Boolean = false
) : GameState {
    companion object
}

/**
 * Converts a [GameState] to a [LensesSupportingGameState].
 */
fun GameState.toLensesSupporting(): LensesSupportingGameState =
    LensesSupportingGameState(levelId, width, height, tiles, moves, pushes, levelCleared)