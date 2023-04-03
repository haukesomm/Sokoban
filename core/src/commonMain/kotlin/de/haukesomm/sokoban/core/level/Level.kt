package de.haukesomm.sokoban.core.level

import kotlin.jvm.JvmField
import kotlin.jvm.JvmRecord

@JvmRecord
data class Level(
    val id: String,
    val name: String,
    @JvmField val width: Int,
    @JvmField val height: Int,
    @JvmField val layoutString: String
)
