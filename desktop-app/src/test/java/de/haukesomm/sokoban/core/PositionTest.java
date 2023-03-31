package de.haukesomm.sokoban.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class PositionTest {

    private List<Position> positionsForIndex(int width, int height) {
        var positions = new LinkedList<Position>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                positions.add(new Position(x, y));
            }
        }
        return positions;
    }

    // In Parmetrisierten Test mit Testfall für je einen Index umwandeln
    @Test
    @DisplayName("Given an index and a target array width, when creating a Position based on it, it's values are correct")
    public void creationFromIndexComputesCorrectPosition() {
        var indexLength = 100;
        assert indexLength % 2 == 0 : "Index length must be even!";

        var sideLength = (int) Math.sqrt(indexLength);
        var positions = positionsForIndex(sideLength, sideLength);

        for (int i = 0; i < indexLength; i++) {
            var expectedPosition = positions.get(i);
            var actualPosition = Position.fromIndex(i, sideLength);
            Assertions.assertEquals(expectedPosition, actualPosition);
        }
    }
}
