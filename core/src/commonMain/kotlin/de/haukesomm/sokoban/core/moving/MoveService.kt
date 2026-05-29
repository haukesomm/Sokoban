package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.model.Direction
import de.haukesomm.sokoban.core.model.GameState
import de.haukesomm.sokoban.core.model.Position

/**
 * Service for moving the player in a [GameState][de.haukesomm.sokoban.core.model.GameState].
 */
interface MoveService {

    /**
     * Tries to compute a new [GameState][de.haukesomm.sokoban.core.model.GameState] in which the
     * [Tile][de.haukesomm.sokoban.core.model.Tile] at the given [position] is moved one step in the given
     * [direction].
     *
     * The method returns `null` if the move is not possible. Otherwise, the method returns a new
     * [GameState][de.haukesomm.sokoban.core.model.GameState].
     */
    fun tryMoveInDirection(
        state: GameState,
        position: Position,
        direction: Direction
    ): GameState?
}