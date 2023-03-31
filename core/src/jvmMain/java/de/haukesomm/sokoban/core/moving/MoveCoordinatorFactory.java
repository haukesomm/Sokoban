package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.moving.validation.*;

import java.util.Set;

public class MoveCoordinatorFactory {

    private MoveCoordinatorFactory() {}

    public static MoveCoordinator newWithDefaultValidators() {
        return new MoveCoordinator(
                new PreconditionMoveValidator(
                        new OutOfBoundsPreventingMoveValidator(),
                        Set.of(
                                new WallCollisionPreventingMoveValidator(),
                                new BoxDetectingMoveValidator(),
                                new MultipleBoxesPreventingMoveValidator()
                        )
                )
        );
    }
}
