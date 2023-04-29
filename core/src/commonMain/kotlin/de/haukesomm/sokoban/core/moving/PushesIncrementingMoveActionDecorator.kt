package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.state.GameState
import de.haukesomm.sokoban.core.state.transform

class PushesIncrementingMoveActionDecorator(private val decorated: MoveAction) : MoveAction {

    override fun performMove(state: GameState): GameState =
        decorated.performMove(state).transform { pushes++ }
}