package de.haukesomm.sokoban.core

import kotlin.math.floor

data class Position(val x: Int, val y: Int) {

    fun nextInDirection(direction: Direction, steps: Int = 1) =
        when(direction) {
            Direction.Top -> copy(y = this.y - steps)
            Direction.Right -> copy(x = this.x + steps)
            Direction.Bottom -> copy(y = this.y + steps)
            Direction.Left -> copy(x = this.x - steps)
        }

    fun toIndex(width: Int): Int = x + (width * y)

    companion object {
        // TODO: Unit test
        fun fromIndex(index: Int, width: Int): Position =
            Position(
                x = index % width,
                y = floor(index.toDouble() / width).toInt()
            )
    }
}
