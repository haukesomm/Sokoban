package de.haukesomm.sokoban.core.level

data class LevelDescription(
    val id: String,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
