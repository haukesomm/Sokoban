package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.core.SokobanGame
import de.haukesomm.sokoban.core.moving.MoveRule
import de.haukesomm.sokoban.core.moving.rules.MultipleBoxesPreventingMoveRule
import de.haukesomm.sokoban.core.moving.rules.WallCollisionPreventingMoveRule

/**
 * Represents a configuration option for a [SokobanGame].
 */
enum class MoveRuleOption(

    /**
     * The name of the configuration option.
     */
    val title: String,

    /**
     * A description of the configuration option.
     */
    val description: String,

    /**
     * Rule to add when the option is applied.
     */
    val moveRule: MoveRule
) {

    /**
     * Configuration option that prevents the user from moving into walls.
     */
    NoWalkingThroughWalls(
        title = "No walking through walls",
        description = "Prevents the user from moving into walls.",
        moveRule = WallCollisionPreventingMoveRule()
    ),

    /**
     * Configuration option that prevents the user from pushing multiple boxes at once.
     */
    PushOneBoxAtATime(
        title = "Push one box at a time",
        description = "Prevents the user from pushing multiple boxes at once.",
        moveRule = MultipleBoxesPreventingMoveRule()
    )
}