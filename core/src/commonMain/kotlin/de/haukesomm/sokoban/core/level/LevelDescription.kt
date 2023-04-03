package de.haukesomm.sokoban.core.level

import kotlin.jvm.JvmField
import kotlin.jvm.JvmRecord

@JvmRecord
data class LevelDescription(
    val id: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
