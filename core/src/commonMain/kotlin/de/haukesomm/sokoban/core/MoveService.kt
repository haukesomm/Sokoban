package de.haukesomm.sokoban.core


/**
 * Service for moving entities in a [GameState].
 *
 * The service provides a [moveEntityIfPossible] method which tries to move an entity in a given direction.
 * If the move is possible, the method returns a new [GameState] with the entity moved. Otherwise, the method
 * returns `null`.
 */
interface MoveService {

    /**
     * Tries to move the entity at the given [position] in the given [direction] in the given [state].
     *
     * The method returns `null` if the move is not possible. Otherwise, the method returns a new [GameState].
     */
    fun moveEntityIfPossible(
        state: GameState,
        position: Position,
        direction: Direction
    ): GameState?
}