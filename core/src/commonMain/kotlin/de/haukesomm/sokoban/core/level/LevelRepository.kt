package de.haukesomm.sokoban.core.level

/**
 * A level repository is a collection of [Level]s that can be played.
 *
 * It provides the [CharacterMap] that is used to parse the [Level.layoutString]s, a list of available [Level]s
 * and a method to get a level by its ID.
 *
 * By default, the [CharacterMaps.default] map is used.
 */
interface LevelRepository {

    /**
     * The [CharacterMap] that is used to parse the [Level.layoutString]s.
     * By default, the [CharacterMaps.default] map is used.
     */
    val characterMap: CharacterMap
        get() = CharacterMaps.default

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
