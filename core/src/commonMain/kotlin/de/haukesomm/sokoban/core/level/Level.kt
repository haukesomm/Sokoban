package de.haukesomm.sokoban.core.level

data class Level(
    val id: String,
    val name: String,
    val width: Int,
    val height: Int,
    val layoutString: String
)
