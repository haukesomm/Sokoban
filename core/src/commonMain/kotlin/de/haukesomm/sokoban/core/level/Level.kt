package de.haukesomm.sokoban.core.level

data class Level(
    val id: String,
    val name: String,
    val width: Int,
    val height: Int,
    val layoutString: String
) {
    fun normalizeLayoutString(): Level =
        layoutString
            .replace(" ", "")
            .replace("(\r)?\n".toRegex(), "")
            .let { copy(layoutString = it) }

    fun toLevelDescription(): LevelDescription = LevelDescription(id, name)
}
