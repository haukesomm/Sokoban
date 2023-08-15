package de.haukesomm.sokoban.core.level

import de.haukesomm.sokoban.core.state.GameState

/**
 * A level is a rectangular grid of game board tiles with a fixed width and height.
 *
 * It has a unique identifier, a name, and a layout string that describes the initial state of the level.
 * The layout string consists of a series of characters that represent the tiles of the level.
 *
 * On a repository-level, no specific encoding of the layout string is enforced. However, an encoding needs to be
 * present in form of a [CharacterMap] when the level is loaded into a [GameState].
 *
 * Here is an example of a layout string using the [CharacterMaps.default] character map. It defines a level with a
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
    val layoutString: String
) {
    /**
     * The normalized layout string is the layout string without any whitespace
     * and line breaks.
     */
    val normalizedLayoutString: String =
        layoutString
            .replace(" ", "")
            .replace("(\r)?\n".toRegex(), "")
}
