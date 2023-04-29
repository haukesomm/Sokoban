package de.haukesomm.sokoban.core

data class Position(val x: Int, val y: Int) {

    fun nextInDirection(direction: Direction) =
        when(direction) {
            Direction.Top -> copy(y = this.y -1)
            Direction.Right -> copy(x = this.x + 1)
            Direction.Bottom -> copy(y = this.y + 1)
            Direction.Left -> copy(x = this.x -1)
        }

    fun toIndex(width: Int): Int = x + (width * y)
}
