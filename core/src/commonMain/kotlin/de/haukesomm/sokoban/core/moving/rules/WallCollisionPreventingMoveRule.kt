package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

class WallCollisionPreventingMoveRule : MoveRule {
    override val title: String = "Don't move trough walls"
    override val description: String = "Prevent entities from moving through walls"

    override fun check(state: GameState, entity: Entity, direction: Direction): Collection<MoveRuleResult> {
        val nextTile = state.nextTileInDirection(entity.position, direction)

        return if (nextTile?.isWall == true) setOf(MoveRuleResult.Impossible)
        else setOf(MoveRuleResult.Possible)
    }
}
