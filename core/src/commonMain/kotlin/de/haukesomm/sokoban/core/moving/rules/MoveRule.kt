package de.haukesomm.sokoban.core.moving.rules

import de.haukesomm.sokoban.core.Direction
import de.haukesomm.sokoban.core.Entity
import de.haukesomm.sokoban.core.Position
import de.haukesomm.sokoban.core.state.GameState

fun interface MoveRule {

    fun check(
        state: GameState,
        position: Position,
        direction: Direction
    ): Collection<MoveRuleResult>
}

fun Collection<MoveRule>.checkAll(
    state: GameState,
    position: Position,
    direction: Direction
): Collection<MoveRuleResult> =
    this.flatMap { it.check(state, position, direction) }.toSet()

fun Array<out MoveRule>.checkAll(
    state: GameState,
    position: Position,
    direction: Direction
): Collection<MoveRuleResult> =
    this.toSet().checkAll(state, position, direction)
