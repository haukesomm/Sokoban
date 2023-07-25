package de.haukesomm.sokoban.core.state

import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Tile
import kotlinx.serialization.Serializable

@Serializable
data class ImmutableGameState(
    override val levelId: String,
    override val width: Int,
    override val height: Int,
    override val tiles: List<Tile>,
    override val moves: Int = 0,
    override val pushes: Int = 0,
    override val levelCleared: Boolean = false
) : GameState

fun GameState.toImmutable(): ImmutableGameState =
    if (this is ImmutableGameState) this
    else ImmutableGameState(levelId, width, height, tiles, moves, pushes, levelCleared)