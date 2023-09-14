package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.level.Level
import de.haukesomm.sokoban.core.level.LevelToGameStateConverter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MoveServiceTests {

    private val converter = LevelToGameStateConverter(
        characterMapOf(
            ' ' to TileProperties(TileType.Empty),
            '#' to TileProperties(TileType.Wall),
            '.' to TileProperties(TileType.Target),
            '@' to TileProperties(TileType.Empty, EntityType.Player),
            'X' to TileProperties(TileType.Empty, EntityType.Box)
        )
    )

    private fun newTestGameState() =
        converter.convert(
            Level(
                id = "test-level",
                name = "Test Level",
                width = 6,
                height = 5,
                layoutString = """
                    ######
                    #_...#
                    #@X__#
                    #_XX_#
                    ######
                """.trimIndent()
            )
        )

    @Test
    fun `With minimal rules, when moving the player, position is updated`() {
        val state = newTestGameState()
        val sut = MoveService.withMinimalRules()

        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Bottom)
        val updatedPosition = result!!.getPlayerPosition()!!

        assertEquals(
            expected = Position(1, 3),
            actual = updatedPosition
        )
    }

    @Test
    fun `With recommended rules, when blocked by a wall, no move is performed`() {
        val state = newTestGameState()
        val sut = MoveService.withRecommendedRules()

        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Left)

        assertNull(result)
    }

    @Test
    fun `With recommended rules, when blocked by a box, box and player are moved`() {
        val state = newTestGameState()
        val sut = MoveService.withRecommendedRules()

        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Right)

        assertTrue(result?.entityAt(Position(2, 2))?.isPlayer ?: false)
        assertTrue(result?.entityAt(Position(3, 2))?.isBox ?: false)
    }

    @Test
    fun `With recommended rules, when blocked by two boxes in a row, none are moved`() {
        val state = newTestGameState()
        val sut = MoveService.withRecommendedRules()

        // Move player down, then right:
        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Bottom)?.let { result1 ->
            sut.moveEntityIfPossible(result1, result1.getPlayerPosition()!!, Direction.Right)
        }

        assertNull(result)
    }
}