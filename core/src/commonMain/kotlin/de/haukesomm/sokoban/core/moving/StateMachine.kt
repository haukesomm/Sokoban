package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.model.Tile

/**
 * State machine for computing the result of trying to move from one [Tile] to another, e.g. moving a box.
 */
object StateMachine {

    /**
     * Represents the result of trying to compute a transition from one [Tile] to another, e.g. moving a box.
     */
    sealed interface TransitionResult {

        /**
         * Represents a valid transition from one [Tile] to another.
         *
         * In order to apply the transition, the first [Tile] passed to [computeTransition] should be changed to
         * [first] and the second tile to the value of [second].
         */
        data class Transform(val first: Tile, val second: Tile) : TransitionResult

        /**
         * Represents a transition that is not directly possible but may be solved by doing another transition first.
         *
         * For example, if the player tries to move into a box, the transition from [Tile.PlayerOnGround] to
         * [Tile.BoxOnGround] is not directly possible, but it may be solved by first moving the box one step in the
         * same direction, which would then allow the player to move into the box's previous position.
         */
        object LookAhead : TransitionResult

        /**
         * Represents a transition that is not possible and cannot be solved by doing another transition first.
         */
        object Abort : TransitionResult
    }

    /**
     * Computes the result of trying to move from the [first] [Tile] to the [second] [Tile].
     *
     * The method returns a [TransitionResult] that represents the result of the transition, e.g. whether the
     * transition is valid, whether it requires a look-ahead, or whether it should be aborted.
     *
     * The result is used by the [StateMachineBasedBasedMoveService] to decide whether a move is possible, another move
     * has to be tried first or to abort the move altogether.
     */
    fun computeTransition(first: Tile, second: Tile): TransitionResult = when (first to second) {
        // Player movements
        Tile.PlayerOnGround to Tile.Ground -> TransitionResult.Transform(Tile.Ground, Tile.PlayerOnGround)
        Tile.PlayerOnGround to Tile.Target -> TransitionResult.Transform(Tile.Ground, Tile.PlayerOnTarget)
        Tile.PlayerOnTarget to Tile.Ground -> TransitionResult.Transform(Tile.Target, Tile.PlayerOnGround)
        Tile.PlayerOnTarget to Tile.Target -> TransitionResult.Transform(Tile.Target, Tile.PlayerOnTarget)

        // Box movements
        Tile.BoxOnGround to Tile.Ground -> TransitionResult.Transform(Tile.Ground, Tile.BoxOnGround)
        Tile.BoxOnGround to Tile.Target -> TransitionResult.Transform(Tile.Ground, Tile.BoxOnTarget)
        Tile.BoxOnTarget to Tile.Ground -> TransitionResult.Transform(Tile.Target, Tile.BoxOnGround)
        Tile.BoxOnTarget to Tile.Target -> TransitionResult.Transform(Tile.Target, Tile.BoxOnTarget)

        // Look-ahead transitions
        // (transitions that are not directly possible but may be solved by doing another transition first)
        Tile.PlayerOnGround to Tile.BoxOnGround, Tile.PlayerOnGround to Tile.BoxOnTarget -> TransitionResult.LookAhead
        Tile.PlayerOnTarget to Tile.BoxOnGround, Tile.PlayerOnTarget to Tile.BoxOnTarget -> TransitionResult.LookAhead

        else -> TransitionResult.Abort
    }
}