package de.haukesomm.sokoban.core.level

/**
 * A level repository is a collection of [Level]s that can be played.
 *
 * It provides a list of available levels and can load a specific level by its identifier.
 */
interface LevelRepository {

    fun getAvailableLevels(): List<LevelDescription>

    fun getLevelOrNull(id: String?): Level?
}

/**
 * Returns the first available level or throws an exception if the repository does not contain any levels.
 *
 * @throws NoSuchElementException if the repository does not contain any levels.
 */
fun LevelRepository.firstOrThrow(): Level =
    getAvailableLevels().firstOrNull()?.let { getLevelOrNull(it.id) }
        ?: throw NoSuchElementException("The repository does not contain any levels!")
