package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.EntityType
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.modify

class SimpleMoveAction(
    private val entity: Entity,
    private val direction: Direction
) : MoveAction {

    override fun performMove(state: GameState): GameState =
        state.modify {
            val nextPosition = entity.position.nextInDirection(direction)

            val movedEntity = entity.copy(facingDirection = direction, position = nextPosition)
            entities.removeAll { it.id == entity.id }
            entities.add(movedEntity)

            moves++
        }
}
