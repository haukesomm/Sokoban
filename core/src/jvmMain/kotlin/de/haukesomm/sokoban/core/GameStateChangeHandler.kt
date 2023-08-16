package de.haukesomm.sokoban.core

import de.haukesomm.sokoban.core.coroutines.SokobanMainScope
import de.haukesomm.sokoban.core.state.GameState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.function.Consumer

/**
 * This object provides a static method to register a [Consumer] at a
 * [SokobanGame] to be notified about changes in the [SokobanGame.state] flow.
 *
 * It is intended to be used in legacy code that is written in Java and does not support the use
 * of flows.
 */
object GameStateChangeHandler {

    @JvmStatic
    fun handle(game: SokobanGame, callback: Consumer<GameState>) {
        game.state
            .onEach(callback::accept)
            .launchIn(SokobanMainScope)
    }
}