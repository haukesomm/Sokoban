package de.haukesomm.sokoban.core

import kotlinx.serialization.Serializable

enum class EntityType {
    Box,
    Player
}

@Serializable
data class Entity(
    val type: EntityType
) {
    val isPlayer: Boolean = type == EntityType.Player
    val isBox: Boolean = type == EntityType.Box
}
