package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.moving.rules.ConditionalMoveRule
import de.haukesomm.sokoban.core.moving.rules.OutOfBoundsPreventingMoveRule
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

object MoveCoordinatorFactory {

    /**
     * Creates a new [MoveCoordinator] with a minimal set of required rules as well as a set of user-replaceable
     * [customRules].
     *
     * The required rules check the following aspects and cannot be overridden:
     * - Prevent out-of-bounds movement ([OutOfBoundsPreventingMoveRule])
     * - Move boxes when the player pushes them ([BoxDetectingMoveRule])
     *
     * The user-configurable rules can be specified via the [customRules] parameter.
     * By default, a set of recommended rules is used. The actual rules can be retrieved from the method's signature.
     */
    @JvmStatic
    @JvmOverloads
    fun create(
        customRules: Collection<MoveRule> = setOf(
            WallCollisionPreventingMoveRule(),
            MultipleBoxesPreventingMoveRule()
        )
    ): MoveCoordinator =
        MoveCoordinator(
            ConditionalMoveRule(
                condition = AggregatingMoveRule(
                    OutOfBoundsPreventingMoveRule(),
                    MoveAfterCompletionPreventingMoveRule()
                ),
                moveRules = setOf(
                    BoxDetectingMoveRule(),
                    *customRules.toTypedArray()
                )
            )
        )
}