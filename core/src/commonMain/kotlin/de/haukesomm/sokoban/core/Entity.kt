package de.haukesomm.sokoban.core

import kotlin.jvm.JvmRecord

@JvmRecord
data class Entity(val id: String, val type: EntityType, val position: Position)
