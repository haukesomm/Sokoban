package de.haukesomm.sokoban.core.level

/**
 * Represents a level that can be loaded from a [LevelRepository].
 *
 * It consists of a unique [id] that is used to load the level from the repository and a [name] that can be displayed
 * to the user.
 */
data class LevelDescription(
    val id: String,
    val name: String
) {
    override fun toString(): String = name
}
