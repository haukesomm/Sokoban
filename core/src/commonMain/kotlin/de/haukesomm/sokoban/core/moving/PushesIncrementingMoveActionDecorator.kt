package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.modify

class PushesIncrementingMoveActionDecorator(
    private val decorated: MoveAction
) : MoveAction {
    override fun performMove(state: GameState): GameState =
        decorated.performMove(state).modify { pushes++ }
}

fun MoveAction.decorateIncrementPushes(): MoveAction =
    PushesIncrementingMoveActionDecorator(this)