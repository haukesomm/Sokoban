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

    // TODO: Make it less complicated to move an entity
    override fun performMove(state: GameState): GameState =
        state.modify {
            // Remove entity from its current tile
            val currentTile = tileAt(entity.position)!!
            val currentTileIndex = tiles.indexOf(currentTile)
            tiles[currentTileIndex] = currentTile.copy(
                entities = currentTile.entities - entity
            )

            // Add entity to Tile at new position
            val nextPosition = entity.position.nextInDirection(direction)
            val entityWithUpdatedPosition = entity.copy(facingDirection = direction, position = nextPosition)
            val nextTile = tileAt(nextPosition)!!
            val nextTileIndex = tiles.indexOf(nextTile)
            tiles[nextTileIndex] = nextTile.copy(
                entities = nextTile.entities + entityWithUpdatedPosition
            )

            moves++
        }
}
