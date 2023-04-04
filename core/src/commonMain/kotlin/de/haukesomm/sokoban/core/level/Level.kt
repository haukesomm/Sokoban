package de.haukesomm.sokoban.core.level

data class Level(
    val id: String,
    val name: String,
    val width: Int,
    val height: Int,
    val layoutString: String
) {
    fun withNormalizedLevelString(): Level {
        val normalized = layoutString.replace(" ", "").replace("(\r)?\n".toRegex(), "")
        return copy(layoutString = normalized)
    }

    fun toLevelDescription(): LevelDescription = LevelDescription(id, name)
}
