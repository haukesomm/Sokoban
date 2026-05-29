package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.levels.GameStateFromLevelFactory
import de.haukesomm.sokoban.core.model.Direction
import de.haukesomm.sokoban.core.model.Level
import de.haukesomm.sokoban.core.model.Position
import de.haukesomm.sokoban.core.model.Tile
import de.haukesomm.sokoban.core.model.biMapOf
import de.haukesomm.sokoban.core.model.getPlayerPosition
import de.haukesomm.sokoban.core.model.tileAt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoveServiceImplTests {

    private fun newTestGameState() =
        GameStateFromLevelFactory.create(
            Level(
                id = "test-level",
                name = "Test Level",
                characterMap = biMapOf(
                    '_' to Tile.Ground,
                    '#' to Tile.Wall,
                    '.' to Tile.Target,
                    '@' to Tile.PlayerOnGround,
                    'X' to Tile.BoxOnGround
                ),
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
    fun `Without rules, when moving the player, position is updated`() {
        val state = newTestGameState()
        val sut = StateMachineBasedBasedMoveService()

        val result = sut.tryMoveInDirection(state, state.getPlayerPosition()!!, Direction.Bottom)
        val updatedPosition = result!!.getPlayerPosition()!!

        assertEquals(
            expected = Position(1, 3),
            actual = updatedPosition
        )
    }

    @Test
    fun `With wall blocking rule, when blocked by a wall, no move is performed`() {
        val state = newTestGameState()
        val sut = StateMachineBasedBasedMoveService()

        val result = sut.tryMoveInDirection(state, state.getPlayerPosition()!!, Direction.Left)

        assertNull(result)
    }

    @Test
    fun `With box detecting rule, when blocked by a box, box and player are moved`() {
        val state = newTestGameState()
        val sut = StateMachineBasedBasedMoveService()

        val result = sut.tryMoveInDirection(state, state.getPlayerPosition()!!, Direction.Right)

        assertEquals(expected = Tile.PlayerOnGround, actual = result?.tileAt(Position(2, 2)))
        assertEquals(expected = Tile.BoxOnGround, actual = result?.tileAt(Position(3, 2)))
    }

    @Test
    fun `With multiple boxes preventing rule, when blocked by two boxes in a row, none are moved`() {
        val state = newTestGameState()
        val sut = StateMachineBasedBasedMoveService()

        // Move player down, then right:
        val result = sut.tryMoveInDirection(state, state.getPlayerPosition()!!, Direction.Bottom)?.let { result1 ->
            sut.tryMoveInDirection(result1, result1.getPlayerPosition()!!, Direction.Right)
        }

        assertNull(result)
    }
}