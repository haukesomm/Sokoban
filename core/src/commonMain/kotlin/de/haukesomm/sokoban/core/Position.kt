package de.haukesomm.sokoban.core

import kotlin.math.floor
import de.haukesomm.sokoban.core.state.GameState

/**
 * Represents a position on the game board denoted by an [x] and [y] coordinate.
 *
 * The position can be converted to an index in the two-dimensional [tiles] list of a [GameState] via the [toIndex]
 * method.
 */
data class Position(val x: Int, val y: Int) {

    /**
     * Returns the next position in the given [direction] or `null` if the next position is out of bounds.
     * Optionally, the number of [steps] can be specified. By default, the next position is returned.
     */
    fun nextInDirection(direction: Direction, steps: Int = 1) =
        when(direction) {
            Direction.Top -> copy(y = this.y - steps)
            Direction.Right -> copy(x = this.x + steps)
            Direction.Bottom -> copy(y = this.y + steps)
            Direction.Left -> copy(x = this.x - steps)
        }

    /**
     * Returns the index of this position in the two-dimensional [GameState.tiles] list of a [GameState] with the given
     * [width].
     */
    fun toIndex(width: Int): Int = x + (width * y)

    companion object {
        // TODO: Unit test
        /**
         * Returns a [Position] from the given [index] in a two-dimensional list with the given [width].
         */
        fun fromIndex(index: Int, width: Int): Position =
            Position(
                x = index % width,
                y = floor(index.toDouble() / width).toInt()
            )
    }
}
