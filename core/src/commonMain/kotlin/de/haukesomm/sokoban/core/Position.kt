package de.haukesomm.sokoban.core

import kotlin.jvm.JvmStatic
import kotlin.math.floor

data class Position(val x: Int, val y: Int) {

    fun nextInDirection(direction: Direction) =
        when(direction) {
            Direction.TOP -> copy(y = this.y -1)
            Direction.RIGHT -> copy(x = this.x + 1)
            Direction.BOTTOM -> copy(y = this.y + 1)
            Direction.LEFT -> copy(x = this.x -1)
        }

    fun toIndex(width: Int): Int = x + (width * y)
}
