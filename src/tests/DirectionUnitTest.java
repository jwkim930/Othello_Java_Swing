package tests;

import backend.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionUnitTest {
    @Test
    public void testConstants() {
        assertEquals("Top", Direction.TOP.toString());
        assertEquals("Top-right", Direction.TOP_RIGHT.toString());
        assertEquals("Right", Direction.RIGHT.toString());
        assertEquals("Bottom-right", Direction.BOTTOM_RIGHT.toString());
        assertEquals("Bottom", Direction.BOTTOM.toString());
        assertEquals("Bottom-left", Direction.BOTTOM_LEFT.toString());
        assertEquals("Left", Direction.LEFT.toString());
        assertEquals("Top-left", Direction.TOP_LEFT.toString());
    }

    @Test
    public void testRotators() {
        Direction dir = Direction.RIGHT;
        assertEquals(Direction.TOP_RIGHT, dir.counterClockwise());
        assertEquals(Direction.BOTTOM_RIGHT, dir.clockwise());
    }

    @Test
    public void testEquals() {
        assertEquals(Direction.TOP, Direction.TOP);
        assertNotEquals(Direction.TOP, Direction.BOTTOM);
        assertNotEquals(Direction.TOP, "Top");
    }
}
