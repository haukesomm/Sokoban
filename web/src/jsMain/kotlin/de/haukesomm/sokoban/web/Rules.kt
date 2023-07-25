package de.haukesomm.sokoban.web

import de.haukesomm.sokoban.core.moving.rules.MoveRule
import de.haukesomm.sokoban.core.moving.rules.MultipleBoxesPreventingMoveRule
import de.haukesomm.sokoban.core.moving.rules.WallCollisionPreventingMoveRule

data class MoveRuleWithDescription(
    val rule: MoveRule,
    val title: String,
    val description: String
)

val Collection<MoveRuleWithDescription>.rules
    get() = map(MoveRuleWithDescription::rule)

/**
 * This object contains all [MoveRule]s that the user can enable or disable.
 */
object UserSelectableRules {

    val rules = listOf(
        MoveRuleWithDescription(
            WallCollisionPreventingMoveRule(),
            title = "No movement through walls",
            description = "Prevents the player from moving into a wall."
        ),
        MoveRuleWithDescription(
            MultipleBoxesPreventingMoveRule(),
            title = "No movement of multiple boxes",
            description = "Prevents the player from moving multiple boxes at once."
        ),
    )
}