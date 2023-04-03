package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.GameState

class SimpleMoveAction(
    private val entity: Entity,
    private val direction: Direction
) : MoveAction {

    override fun performMove(state: GameState): GameState {
        val entities = state.entities.toMutableSet()
        entities.remove(entity)

        val movedEntity = Entity(
            entity.id,
            entity.type,
            entity.position.nextInDirection(direction)
        )
        entities.add(movedEntity)

        return GameState(state.tiles, entities)
    }
}
