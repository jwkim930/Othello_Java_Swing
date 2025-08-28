package tests;

import backend.Board;
import backend.MockBoard;
import entities.Stone;
import gui.SquarePanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MockBoardUnitTest {
    int defaultBoardSize = 8;

    @BeforeEach
    void initialize() {
        Board.initialize(this.defaultBoardSize);
    }

    @AfterEach
    void reset() {
        Board.reset();
        SquarePanel.resetSquareSize();
    }

    @Nested
    class testConstructor {
        @AfterEach
        void checkCopyResult() {
            MockBoard mBoard = new MockBoard();
            for (int row = 0; row < defaultBoardSize; row++) {
                for (int col = 0; col < defaultBoardSize; col++) {
                    assertEquals(Board.getInstance().getSquareAt(row, col).getStone(), mBoard.getStoneAt(row, col));
                }
            }
        }

        @Test
        void empty() {
            // do nothing
        }

        @Test
        void oneStone() {
            Board.getInstance().getSquareAt(0, 0).place(Stone.BLACK);
        }

        @Test
        void regularGame() {
            Board board = Board.getInstance();
            board.getSquareAt(3, 3).place(Stone.WHITE);
            board.getSquareAt(3, 4).place(Stone.BLACK);
            board.getSquareAt(4, 3).place(Stone.BLACK);
            board.getSquareAt(4, 4).place(Stone.WHITE);
        }
    }

    @Test
    void testEquals() {
        Board.getInstance().getSquareAt(0, 0).place(Stone.BLACK);
        MockBoard mBoard1 = new MockBoard();
        MockBoard mBoard2 = new MockBoard();
        assertEquals(mBoard1, mBoard2);
        Board.getInstance().getSquareAt(3, 3).place(Stone.WHITE);
        MockBoard mBoard3 = new MockBoard();
        assertNotEquals(mBoard1, mBoard3);
    }

    @Nested
    class testPlaceStone {
        @Test
        void oneFlip() {
            Board.getInstance().getSquareAt(0, 0).place(Stone.BLACK);
            Board.getInstance().getSquareAt(0, 1).place(Stone.WHITE);
            MockBoard mBoard = new MockBoard();
            assertEquals(1, mBoard.placeStone(0, 2));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(0, 0));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(0, 1));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(0, 2));
        }

        @Test
        void multipleHorizontal() {
            Board.getInstance().getSquareAt(0, 0).place(Stone.WHITE);
            for (int i = 1; i < 7; i++) {
                Board.getInstance().getSquareAt(0, i).place(Stone.BLACK);
            }
            Board.getInstance().nextTurn();
            MockBoard mBoard = new MockBoard();
            assertEquals(6, mBoard.placeStone(0, 7));
            for (int i = 0; i < 8; i++) {
                assertEquals(Stone.WHITE, mBoard.getStoneAt(0, i));
            }
        }

        @Test
        void multipleVertical() {
            Board.getInstance().getSquareAt(0, 2).place(Stone.BLACK);
            for (int i = 1; i < 7; i++) {
                Board.getInstance().getSquareAt(i, 2).place(Stone.WHITE);
            }
            MockBoard mBoard = new MockBoard();
            assertEquals(6, mBoard.placeStone(7, 2));
            for (int i = 0; i < 8; i++) {
                assertEquals(Stone.BLACK, mBoard.getStoneAt(i, 2));
            }
        }

        @Test
        void multipleMix() {
            Board.getInstance().getSquareAt(1, 1).place(Stone.BLACK);
            Board.getInstance().getSquareAt(5, 1).place(Stone.BLACK);
            Board.getInstance().getSquareAt(1, 5).place(Stone.BLACK);
            Board.getInstance().getSquareAt(5, 5).place(Stone.BLACK);
            Board.getInstance().getSquareAt(2, 2).place(Stone.WHITE);
            Board.getInstance().getSquareAt(4, 2).place(Stone.WHITE);
            Board.getInstance().getSquareAt(2, 4).place(Stone.WHITE);
            Board.getInstance().getSquareAt(4, 4).place(Stone.WHITE);
            MockBoard mBoard = new MockBoard();
            assertEquals(4, mBoard.placeStone(3, 3));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(1, 1));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(5, 1));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(1, 5));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(5, 5));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(2, 2));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(4, 2));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(2, 4));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(4, 4));
            assertEquals(Stone.BLACK, mBoard.getStoneAt(3, 3));
        }

        @Test
        void invalid() {
            MockBoard mBoard = new MockBoard();
            assertEquals(0, mBoard.placeStone(0, 0));
        }
    }

    @Test
    void testCopy() {
        Board.getInstance().getSquareAt(0, 0).place(Stone.BLACK);
        Board.getInstance().getSquareAt(0, 1).place(Stone.WHITE);
        MockBoard mBoard1 = new MockBoard();
        MockBoard mBoard2 = mBoard1.copy();
        assertEquals(mBoard1, mBoard2);
        assertNotSame(mBoard1, mBoard2);
        mBoard2.placeStone(0, 2);   // first turn is black so it should work
        assertNotEquals(mBoard1, mBoard2);
    }

    @Nested
    class testGetValidMoves {
        @Test
        void one() {
            Board.getInstance().getSquareAt(0, 0).place(Stone.BLACK);
            Board.getInstance().getSquareAt(0, 1).place(Stone.WHITE);
            MockBoard mBoard = new MockBoard();
            List<int[]> result = mBoard.getValidMoves();
            assertEquals(1, result.size());
            assertEquals(0, result.get(0)[0]);
            assertEquals(2, result.get(0)[1]);
        }

        @Test
        void two() {
            Board.getInstance().getSquareAt(0, 0).place(Stone.BLACK);
            Board.getInstance().getSquareAt(0, 1).place(Stone.WHITE);
            Board.getInstance().getSquareAt(1, 0).place(Stone.WHITE);
            MockBoard mBoard = new MockBoard();
            List<int[]> result = mBoard.getValidMoves();
            assertEquals(2, result.size());
            int[] exp1 = {0, 2};
            int[] exp2 = {2, 0};
            assertTrue(Arrays.equals(exp1, result.get(0)) || Arrays.equals(exp1, result.get(1)));
            assertTrue(Arrays.equals(exp2, result.get(0)) || Arrays.equals(exp2, result.get(1)));
        }

        @Test
        void none() {
            MockBoard mBoard = new MockBoard();
            List<int[]> result = mBoard.getValidMoves();
            assertEquals(0, result.size());
        }
    }

    @Test
    void testParse() {
        // create a test file
        String fileName = "test_board";
        while ((new File(fileName)).isFile()) {
            fileName = fileName + "0";
        }
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println("W");
            out.println("8");
            out.println("********");
            out.println("********");
            out.println("***W****");
            out.println("****B***");
            out.println("***B****");
            out.println("***BW***");
            out.println("********");
            out.println("********");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // read the test file and test the result
        MockBoard mBoard = MockBoard.parse(fileName);
        assertEquals(Stone.WHITE, mBoard.getTurn());
        assertEquals(8, mBoard.getSize());
        int[][] whites = { {2, 3}, {5, 4} };
        int[][] blacks = { {3, 4}, {4, 3}, {5, 3} };
        for (int row = 0; row < mBoard.getSize(); row++) {
            for (int col = 0; col < mBoard.getSize(); col++) {
                boolean shouldBeWhite = false;
                boolean shouldBeBlack = false;
                for (int[] white : whites) {
                    if (Arrays.equals(white, new int[] {row, col})) {
                        shouldBeWhite = true;
                        break;
                    }
                }
                if (!shouldBeWhite) {
                    for (int[] black : blacks) {
                        if (Arrays.equals(black, new int[] {row, col})) {
                            shouldBeBlack = true;
                            break;
                        }
                    }
                }

                if (shouldBeWhite) {
                    assertEquals(Stone.WHITE, mBoard.getStoneAt(row, col));
                }
                else if (shouldBeBlack) {
                    assertEquals(Stone.BLACK, mBoard.getStoneAt(row, col));
                }
                else {
                    assertNull(mBoard.getStoneAt(row, col));
                }
            }

            // remove the test file
            (new File(fileName)).delete();
        }
    }
}
