package tests;

import backend.Board;
import entities.Direction;
import gui.SquarePanel;
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
        dir = Direction.TOP;
        assertEquals(Direction.TOP_LEFT, dir.counterClockwise());
        dir = Direction.TOP_LEFT;
        assertEquals(Direction.TOP, dir.clockwise());
    }

    @Test
    public void testEquals() {
        assertEquals(Direction.TOP, Direction.TOP);
        assertNotEquals(Direction.TOP, Direction.BOTTOM);
        assertNotEquals(Direction.TOP, "Top");
    }

    @Test
    public void testMover() {
        Board.initialize(8);
        // test in-bound
        int[] start = {2, 2};
        assertArrayEquals(new int[] {1, 2}, Direction.TOP.moveThisWay(start[0], start[1]));
        assertArrayEquals(new int[] {1, 3}, Direction.TOP_RIGHT.moveThisWay(start[0], start[1]));
        assertArrayEquals(new int[] {2, 3}, Direction.RIGHT.moveThisWay(start[0], start[1]));
        assertArrayEquals(new int[] {3, 3}, Direction.BOTTOM_RIGHT.moveThisWay(start[0], start[1]));
        assertArrayEquals(new int[] {3, 2}, Direction.BOTTOM.moveThisWay(start[0], start[1]));
        assertArrayEquals(new int[] {3, 1}, Direction.BOTTOM_LEFT.moveThisWay(start[0], start[1]));
        assertArrayEquals(new int[] {2, 1}, Direction.LEFT.moveThisWay(start[0], start[1]));
        assertArrayEquals(new int[] {1, 1}, Direction.TOP_LEFT.moveThisWay(start[0], start[1]));
        // test out-bound
        assertNull(Direction.TOP.moveThisWay(0, 2));
        assertNull(Direction.TOP_RIGHT.moveThisWay(0, 2));
        assertNull(Direction.TOP_RIGHT.moveThisWay(2, 7));
        assertNull(Direction.RIGHT.moveThisWay(2, 7));
        assertNull(Direction.BOTTOM_RIGHT.moveThisWay(2, 7));
        assertNull(Direction.BOTTOM_RIGHT.moveThisWay(7, 2));
        assertNull(Direction.BOTTOM.moveThisWay(7, 2));
        assertNull(Direction.BOTTOM_LEFT.moveThisWay(7, 2));
        assertNull(Direction.BOTTOM_LEFT.moveThisWay(2, 0));
        assertNull(Direction.LEFT.moveThisWay(2, 0));
        assertNull(Direction.TOP_LEFT.moveThisWay(2, 0));
        assertNull(Direction.TOP_LEFT.moveThisWay(0, 2));
        Board.reset();
        SquarePanel.resetSquareSize();
    }
}
