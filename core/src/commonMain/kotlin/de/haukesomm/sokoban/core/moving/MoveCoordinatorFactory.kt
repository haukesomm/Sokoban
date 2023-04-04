package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.moving.validation.*
import kotlin.jvm.JvmStatic

object MoveCoordinatorFactory {

    @JvmStatic
    fun newWithDefaultValidators(): MoveCoordinator {
        return MoveCoordinator(
            PreconditionMoveValidator(
                OutOfBoundsPreventingMoveValidator(),
                setOf(
                    WallCollisionPreventingMoveValidator(),
                    BoxDetectingMoveValidator(),
                    MultipleBoxesPreventingMoveValidator()
                )
            )
        )
    }
}
