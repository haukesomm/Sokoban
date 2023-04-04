package de.haukesomm.sokoban.core

data class Entity(
    val id: String = Id.next(),
    val type: EntityType,
    val position: Position,
    val facingDirection: Direction
)
