package de.haukesomm.sokoban.core

data class Entity(
    val id: String = Id.next(),
    val type: Type,
    val position: Position
) {
    enum class Type { Box, Player }

    val isPlayer: Boolean = type == Type.Player
    val isBox: Boolean = type == Type.Box
}
