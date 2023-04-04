package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.GameState

interface MoveAction {

    fun performMove(state: GameState): GameState
}
