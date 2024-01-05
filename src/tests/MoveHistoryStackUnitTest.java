package tests;

import backend.MoveHistory;
import backend.MoveHistoryStack;
import entities.Stone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveHistoryStackUnitTest {
    MoveHistoryStack histories;

    @BeforeEach
    public void preTest() {
        this.histories = new MoveHistoryStack(Stone.BLACK, 4, 5, new int[][] {{4, 4}});
        this.histories.push(Stone.WHITE, 5, 5, new int[][] {{4, 4}});
        this.histories.push(Stone.BLACK, 6, 5, new int[][] {{5, 5}});
        this.histories.push(Stone.WHITE, 6, 6, new int[][] {{5, 5}});
    }

    @Test
    public void testCurrent() {
        MoveHistory expectedMove = new MoveHistory(Stone.WHITE, 6, 6, new int[][] {{5, 5}});
        int expectedPosition = 3;
        MoveHistory actualMove = this.histories.current();
        int actualPosition = this.histories.getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
    }

    @Test
    public void testPrevious() {
        MoveHistory expectedMove = new MoveHistory(Stone.WHITE, 6, 6, new int[][] {{5, 5}});
        int expectedPosition = 2;
        MoveHistory actualMove = this.histories.previous();
        int actualPosition = this.histories.getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
    }

    @Test
    public void testPreviousBeforeFirstMove() {
        int expectedPosition = -1;
        for (int i = 0; i < 4; i++) {
            this.histories.previous();
        }
        MoveHistory expectedMove = null;
        MoveHistory actualMove = this.histories.previous();
        int actualPosition = this.histories.getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
    }

    @Test
    public void testNextAtLastMove() {
        MoveHistory expectedMove = null;
        int expectedPosition = 3;
        MoveHistory actualMove = this.histories.next();
        int actualPosition = this.histories.getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
    }

    @Test
    public void testNext() {
        this.histories.previous();
        MoveHistory expectedMove = new MoveHistory(Stone.WHITE, 6, 6, new int[][] {{5, 5}});
        int expectedPosition = 3;
        MoveHistory actualMove = this.histories.next();
        int actualPosition = this.histories.getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
    }

    @Test
    public void testNextBeforeFirstMove() {
        for (int i = 0; i < 4; i++) {
            this.histories.previous();
        }
        MoveHistory expectedMove = new MoveHistory(Stone.BLACK, 4, 5, new int[][] {{4, 4}});
        int expectedPosition = 0;
        MoveHistory actualMove = this.histories.next();
        int actualPosition = this.histories.getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
    }

    @Test
    public void testPush() {
        this.histories.push(Stone.WHITE, 6, 7, new int[][] {{6, 6}});
        MoveHistory expectedMove = new MoveHistory(Stone.WHITE, 6, 7, new int[][] {{6, 6}});
        int expectedPosition = 4;
        MoveHistory actualMove = this.histories.current();
        int actualPosition = this.histories.getCurrentPosition();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
    }

    @Test
    public void testPushMiddle() {
        for (int i = 0; i < 2; i++) {
            this.histories.previous();
        }
        this.histories.push(Stone.BLACK, 3, 2, new int[][] {{3, 3}});
        MoveHistory expectedMove = new MoveHistory(Stone.BLACK, 3, 2, new int[][] {{3, 3}});
        int expectedPosition = 2;
        int expectedSize = 3;
        MoveHistory actualMove = this.histories.current();
        int actualPosition = this.histories.getCurrentPosition();
        int actualSize = this.histories.size();
        assertEquals(expectedPosition, actualPosition);
        assertEquals(expectedMove, actualMove);
        assertEquals(expectedSize, actualSize);
    }
}
