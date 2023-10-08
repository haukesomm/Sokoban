package de.haukesomm.sokoban.core

/**
 * A level repository is a collection of [Level]s that can be played.
 *
 * It provides the [CharacterMap] that is used to parse the [Level.layoutString]s, a list of available [Level]s
 * and a method to get a level by its ID.
 *
 * By default, the [CharacterMap.default] map is used.
 */
interface LevelRepository {

    /**
     * Returns a list of all available levels.
     */
    fun getAvailableLevels(): List<LevelDescription>

    /**
     * Loads the level with the given [id] or returns `null` if the level does not exist.
     */
    fun getLevelOrNull(id: String?): Level?

    /**
     * Returns the next level after the given [currentLevelId] or `null` if there is no next level.
     */
    fun getNextLevel(currentLevelId: String): LevelDescription?
}

/**
 * Returns the first available level or throws an exception if the repository does not contain any levels.
 *
 * @throws NoSuchElementException if the repository does not contain any levels.
 */
fun LevelRepository.firstOrThrow(): Level =
    getAvailableLevels().firstOrNull()?.let { getLevelOrNull(it.id) }
        ?: throw NoSuchElementException("The repository does not contain any levels!")
