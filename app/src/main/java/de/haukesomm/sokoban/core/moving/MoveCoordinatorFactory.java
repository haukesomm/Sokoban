package de.haukesomm.sokoban.core.moving;

import de.haukesomm.sokoban.core.moving.checkers.BoxDetectingMoveChecker;
import de.haukesomm.sokoban.core.moving.checkers.MultipleBoxesPreventingMoveChecker;
import de.haukesomm.sokoban.core.moving.checkers.WallCollisionMoveChecker;

public class MoveCoordinatorFactory {

    private MoveCoordinatorFactory() {}

    public static MoveCoordinator newWithDefaultValidators() {
        return new MoveCoordinator(
                new WallCollisionMoveChecker(),
                new BoxDetectingMoveChecker(),
                new MultipleBoxesPreventingMoveChecker()
        );
    }
}
