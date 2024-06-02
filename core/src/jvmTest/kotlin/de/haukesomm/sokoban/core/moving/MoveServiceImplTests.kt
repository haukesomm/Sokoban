package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.*
import de.haukesomm.sokoban.core.moving.rules.BoxDetectingMoveRule
import de.haukesomm.sokoban.core.moving.rules.MultipleBoxesPreventingMoveRule
import de.haukesomm.sokoban.core.moving.rules.WallCollisionPreventingMoveRule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MoveServiceImplTests {

    private fun newTestGameState() =
        ImmutableGameState.fromLevel(
            Level(
                id = "test-level",
                name = "Test Level",
                characterMap = biMapOf(
                    '_' to TileProperties(TileType.Empty),
                    '#' to TileProperties(TileType.Wall),
                    '.' to TileProperties(TileType.Target),
                    '@' to TileProperties(TileType.Empty, EntityType.Player),
                    'X' to TileProperties(TileType.Empty, EntityType.Box)
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
        val sut = MoveServiceImpl()

        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Bottom)
        val updatedPosition = result!!.getPlayerPosition()!!

        assertEquals(
            expected = Position(1, 3),
            actual = updatedPosition
        )
    }

    @Test
    fun `With wall blocking rule, when blocked by a wall, no move is performed`() {
        val state = newTestGameState()
        val sut = MoveServiceImpl(WallCollisionPreventingMoveRule())

        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Left)

        assertNull(result)
    }

    @Test
    fun `With box detecting rule, when blocked by a box, box and player are moved`() {
        val state = newTestGameState()
        val sut = MoveServiceImpl(BoxDetectingMoveRule())

        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Right)

        assertTrue(result?.entityAt(Position(2, 2))?.isPlayer ?: false)
        assertTrue(result?.entityAt(Position(3, 2))?.isBox ?: false)
    }

    @Test
    fun `With multiple boxes preventing rule, when blocked by two boxes in a row, none are moved`() {
        val state = newTestGameState()
        val sut = MoveServiceImpl(
            BoxDetectingMoveRule(),
            MultipleBoxesPreventingMoveRule()
        )

        // Move player down, then right:
        val result = sut.moveEntityIfPossible(state, state.getPlayerPosition()!!, Direction.Bottom)?.let { result1 ->
            sut.moveEntityIfPossible(result1, result1.getPlayerPosition()!!, Direction.Right)
        }

        assertNull(result)
    }
}