package de.haukesomm.sokoban.core.level

interface LevelRepository {

    fun getAvailableLevels(): List<LevelDescription>

    fun getLevelOrNull(id: String?): Level?
}

fun LevelRepository.firstOrThrow(): Level =
    getAvailableLevels()
        .first()
        .let {
            getLevelOrNull(it.id)
                ?: throw NoSuchElementException("The repository does not contain any levels!")
        }
