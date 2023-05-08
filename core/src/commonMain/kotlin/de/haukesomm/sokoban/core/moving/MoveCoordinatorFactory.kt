package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.moving.rules.*
import de.haukesomm.sokoban.core.moving.rules.ConditionalMoveRule
import de.haukesomm.sokoban.core.moving.rules.OutOfBoundsPreventingMoveRule
import kotlin.jvm.JvmStatic

object MoveCoordinatorFactory {

    @JvmStatic
    fun withDefaultRules(): MoveCoordinator =
        MoveCoordinator(
            ConditionalMoveRule(
                condition = OutOfBoundsPreventingMoveRule(),
                moveRules = setOf(
                    WallCollisionPreventingMoveRule(),
                    BoxDetectingMoveRule(),
                    MultipleBoxesPreventingMoveRule()
                )
            )
        )

    @JvmStatic
    fun withMinimalRecommendedRules(additional: Set<MoveRule> = emptySet()): MoveCoordinator =
        MoveCoordinator(
            ConditionalMoveRule(
                condition = OutOfBoundsPreventingMoveRule(),
                moveRules = setOf(
                    BoxDetectingMoveRule(),
                    *additional.toTypedArray()
                )
            )
        )
}