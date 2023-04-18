package de.haukesomm.sokoban.core.moving.validation

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.TileType

class WallCollisionPreventingMoveValidator : AbstractMoveValidator() {

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveValidatorStatus> {
        val nextTile = state.getNextTileInDirectionOrNull(entity.position, direction)
        return if (nextTile != null && nextTile.type === TileType.WALL) {
            singleResult(MoveValidatorStatus.IMPOSSIBLE)
        } else singleResult(MoveValidatorStatus.POSSIBLE)
    }
}
