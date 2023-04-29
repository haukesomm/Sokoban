package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.state.GameState

interface MoveRule {
    val title: String
    val description: String

    fun check(
        state: GameState,
        entity: Entity,
        direction: Direction
    ): Collection<MoveRuleResult>
}

fun Collection<MoveRule>.checkAll(
    state: GameState,
    entity: Entity,
    direction: Direction
): Collection<MoveRuleResult> =
    this.flatMap { it.check(state, entity, direction) }.toSet()
