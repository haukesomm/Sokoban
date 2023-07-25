package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform

class SimpleMoveAction(
    private val position: Position,
    private val direction: Direction
) : MoveAction {

    override fun performMove(state: GameState): GameState =
        state.transform {
            val currentIndex = position.toIndex(state.width)
            val entity = tiles[currentIndex].entity
            tiles[currentIndex] = tiles[currentIndex].copy(entity = null)

            val nextIndex = position.nextInDirection(direction).toIndex(state.width)
            tiles[nextIndex] = tiles[nextIndex].copy(entity = entity)

            moves++
        }
}
