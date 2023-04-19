package de.haukesomm.sokoban.web.model

import de.haukesomm.sokoban.core.Tile
import de.haukesomm.sokoban.core.state.GameState
import dev.fritz2.core.Lenses

/**
 * Wrapper [GameState] enabling the usage of [Lenses].
 *
 * __Important__: Custom implementations of the individual _properties_ such as `width`, `height`, etc.
 * will be overridden when using this decorator!
 */
@Lenses
data class LensesGameStateDecorator(
    private val decorated: GameState,
    override val levelId: String,
    override val width: Int,
    override val height: Int,
    override val tiles: List<Tile>,
    override val moves: Int,
    override val pushes: Int,
    override val levelCleared: Boolean,
) : GameState by decorated {
    companion object
}

/**
 * Creates a wrapper [GameState] enabling the usage of [Lenses].
 *
 * __Important__: Custom implementations of the individual _properties_ such as `width`, `height`, etc.
 * will be overridden when using this decorator!
 */
fun GameState.withLenses(): LensesGameStateDecorator =
    LensesGameStateDecorator(
        this,
        levelId,
        width,
        height,
        tiles,
        moves,
        pushes,
        levelCleared
    )