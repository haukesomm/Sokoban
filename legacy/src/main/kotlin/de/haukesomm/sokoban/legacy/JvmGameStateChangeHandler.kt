package de.haukesomm.sokoban.legacy

import de.haukesomm.sokoban.core.GameState
import de.haukesomm.sokoban.core.SokobanGame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.ForkJoinPool
import java.util.function.Consumer

/**
 * This object provides a static method to register a [Consumer] at a
 * [SokobanGame] to be notified about changes in the [SokobanGame.state] flow.
 *
 * It is intended to be used in legacy code that is written in Java and does not support the use
 * of flows.
 */
object JvmGameStateChangeHandler {

    private val coroutineScope = CoroutineScope(ForkJoinPool.commonPool().asCoroutineDispatcher())

    /**
     * Invokes the given [callback] each time the [game] emits a new [GameState].
     */
    @JvmStatic
    fun handle(game: SokobanGame, callback: Consumer<GameState>) {
        game.state
            .onEach(callback::accept)
            .launchIn(coroutineScope)
    }
}