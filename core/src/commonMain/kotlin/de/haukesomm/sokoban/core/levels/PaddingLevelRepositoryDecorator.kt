package de.haukesomm.sokoban.core.levels

import de.haukesomm.sokoban.core.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max

/**
 * Decorates a [LevelRepository] and adds a padding with empty tiles around the level.
 *
 * The padding is added to the left and right side of the level, if the level is smaller than [minWidth],
 * and to the top and bottom side, if the level is smaller than [minHeight].
 */
class PaddingLevelRepositoryDecorator(
    private val decorated: LevelRepository,
    private val minWidth: Int,
    private val minHeight: Int
) : LevelRepository by decorated {

    private fun StringBuilder.addLines(lines: Int, width: Int, character: Char) {
        repeat(lines) {
            repeat(width) {
                append(character)
            }
            appendLine()
        }
    }

    private fun StringBuilder.wrapLine(line: String, left: Int, right: Int, character: Char) {
        repeat(left) {
            append(character)
        }
        append(line)
        repeat(right) {
            append(character)
        }
        appendLine()
    }

    override fun getLevelOrNull(id: String?): Level? {
        val level = decorated.getLevelOrNull(id)
            ?: return null

        val horizontalPadding = max((minWidth - level.width) / 2.0, 0.0)
        val verticalPadding = max((minHeight - level.height) / 2.0, 0.0)

        val paddingLeft = floor(horizontalPadding).toInt()
        val paddingRight = ceil(horizontalPadding).toInt()
        val paddingTop = floor(verticalPadding).toInt()
        val paddingBottom = ceil(verticalPadding).toInt()

        val paddedLayout = buildString {
            val paddingCharacter = level.characterMap.inverse[TileProperties(TileType.Empty)]
                ?: throw IllegalStateException("No empty tile in character map")

            val paddedWidth = paddingLeft + level.width + paddingRight

            addLines(paddingTop, paddedWidth, paddingCharacter)
            level.layoutString.lines().forEach { line ->
                wrapLine(line, paddingLeft, paddingRight, paddingCharacter)
            }
            addLines(paddingBottom, paddedWidth, paddingCharacter)
        }.trim()

        return level.copy(layoutString = paddedLayout)
    }
}