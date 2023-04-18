package de.haukesomm.sokoban.core

data class Entity(
    val id: String = Id.next(),
    val type: EntityType,
    val position: Position,
    val facingDirection: Direction = Direction.TOP
) {
    val isPlayer: Boolean = type == EntityType.PLAYER
    val isBox: Boolean = type == EntityType.BOX
}
