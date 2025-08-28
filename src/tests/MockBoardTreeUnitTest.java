package tests;

import backend.Board;
import backend.MockBoard;
import backend.MockBoardTree;
import entities.Stone;
import gui.SquarePanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MockBoardTreeUnitTest {
    private int boardDefaultSize = 8;

    @BeforeEach
    void initialize() {
        Board.initialize(boardDefaultSize);
        // set initial game state
        Board.getInstance().getSquareAt(3, 3).place(Stone.WHITE);
        Board.getInstance().getSquareAt(3, 4).place(Stone.BLACK);
        Board.getInstance().getSquareAt(4, 3).place(Stone.BLACK);
        Board.getInstance().getSquareAt(4, 4).place(Stone.WHITE);
    }

    @AfterEach
    void reset() {
        Board.reset();
        SquarePanel.resetSquareSize();
    }

    @Test
    void testConstructors() {
        MockBoard mBoard = new MockBoard();
        MockBoardTree tree1 = new MockBoardTree(mBoard);
        MockBoardTree tree2 = new MockBoardTree();
        assertEquals(mBoard, tree1.getMockBoard());
        assertNull(tree1.getParent());
        assertEquals(0, tree1.size());
        assertEquals(mBoard, tree2.getMockBoard());
    }

    @Test
    void testGenerateAllOutcomes() {
        MockBoard mBoard = new MockBoard();
        MockBoardTree tree = new MockBoardTree(mBoard);
        assertEquals(4, tree.generateAllOutcomes());
        assertEquals(4, tree.size());
        assertEquals(mBoard, tree.getMockBoard());
        int[][] validMoves = {{2, 3}, {3, 2}, {4, 5}, {5, 4}};
        for (int i = 0; i < 4; i++) {
            boolean valid = false;
            for (int[] move : validMoves) {
                if (Stone.BLACK.equals(tree.getChild(i).getMockBoard().getStoneAt(move[0], move[1]))) {
                    valid = true;
                    assertArrayEquals(move, tree.getChild(i).getPreviousMove());
                    break;
                }
            }
            assertTrue(valid);
        }
    }

    @Test
    void testGenerateAllOutcomesNoMove() {
        MockBoard mBoard = MockBoard.parse("src/tests/no_move_board.txt");
        MockBoardTree tree = new MockBoardTree(mBoard);
        tree.generateAllOutcomes();   // should've passed the turn
        assertEquals(1, tree.size());
        assertSame(tree, tree.getChild(0).getParent());
        MockBoard childBoard = tree.getChild(0).getMockBoard();
        assertEquals(Stone.BLACK, childBoard.getTurn());
        assertEquals(mBoard.countStones(Stone.WHITE), childBoard.countStones(Stone.WHITE));
        assertEquals(mBoard.countStones(Stone.BLACK), childBoard.countStones(Stone.BLACK));
    }

    @Test
    void testDeepGenerateAllOutcomes() {
        MockBoard mBoard = new MockBoard();
        MockBoardTree tree = new MockBoardTree(mBoard);
        tree.generateAllOutcomes();   // should have 4 children now
        tree.getChild(0).generateAllOutcomes();   // first child should have children now
        tree.deepGenerateAllOutcomes();
        // not-first children should have children now
        assertTrue(tree.getChild(1).size() > 0);
        assertEquals(0, tree.getChild(1).getChild(0).size());
        assertTrue(tree.getChild(2).size() > 0);
        assertEquals(0, tree.getChild(2).getChild(0).size());
        assertTrue(tree.getChild(3).size() > 0);
        assertEquals(0, tree.getChild(3).getChild(0).size());
        // first child should have grandchildren
        assertTrue(tree.getChild(0).getChild(0).size() > 0);
        assertEquals(0, tree.getChild(0).getChild(0).getChild(0).size());
    }
}
