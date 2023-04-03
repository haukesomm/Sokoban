package de.haukesomm.sokoban.core.moving

import de.haukesomm.sokoban.core.moving.validation.*

object MoveCoordinatorFactory {

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
