package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.state.GameState

interface MoveAction {

    fun performMove(state: GameState): GameState
}
