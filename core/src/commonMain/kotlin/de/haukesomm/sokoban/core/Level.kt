package de.haukesomm.sokoban.core

/**
 * Represents a [Level] that can be loaded from a [LevelRepository].
 *
 * It consists of a unique [id] that is used to load the level from the repository and a [name] that can be displayed
 * to the user.
 */
data class LevelDescription(
    val id: String,
    val name: String
)

/**
 * A level is a rectangular grid of game board tiles with a fixed width and height.
 *
 * It has a unique identifier, a name, and a layout string that describes the initial state of the level.
 * The layout string consists of a series of characters that represent the tiles of the level.
 *
 * The layout string has to have [width] * [height] characters and must only use the characters defined in the
 * [characterMap].
 *
 * Here is an example of a layout string using the [CharacterMap.default] character map. It defines a level with a
 * width of 8 and a height of 5. It contains a player (`@`), a box (`$`), and a target (`.`).
 * ```
 * ########
 * #      #
 * # @ $. #
 * #      #
 * ########
 * ```
 */
data class Level(
    val id: String,
    val name: String,
    val width: Int,
    val height: Int,
    val layoutString: String,
    val characterMap: CharacterMap = CharacterMap.default
) {

    /**
     * The normalized layout string is the layout string without any whitespace
     * and line breaks.
     */
    val normalizedLayoutString: String by lazy {
        layoutString.replace("[ \r\n]+".toRegex(), "")
    }
}
