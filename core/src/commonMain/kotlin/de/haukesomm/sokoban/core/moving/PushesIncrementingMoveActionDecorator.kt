package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.GameState

class PushesIncrementingMoveActionDecorator(
    private val decorated: MoveAction
) : MoveAction {
    override fun performMove(state: GameState): GameState =
        decorated.performMove(state).copy(pushes = state.pushes + 1)
}

fun MoveAction.decorateIncrementPushes(): MoveAction =
    PushesIncrementingMoveActionDecorator(this)