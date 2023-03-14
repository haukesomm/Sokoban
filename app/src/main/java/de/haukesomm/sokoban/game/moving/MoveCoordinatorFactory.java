package de.haukesomm.sokoban.game.moving;

import de.haukesomm.sokoban.game.moving.checkers.BoxDetectingMoveChecker;
import de.haukesomm.sokoban.game.moving.checkers.MultipleBoxesPreventingMoveChecker;
import de.haukesomm.sokoban.game.moving.checkers.WallCollisionMoveChecker;

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
