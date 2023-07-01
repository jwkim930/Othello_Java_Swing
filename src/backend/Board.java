package backend;

import gui.SquarePanel;

public class Board {
    /**
     * Contains all squares in the board. Access by [row][col].
     */
    private SquarePanel[][] squares;
    /**
     * The size of the board. The board is square, so this is both width and height.
     * When the board is not initialized, this is -1.
     */
    private static int size = -1;

    public Board() {
        // stub for now
    }

    /**
     * Sets the board size. Can only be called once.
     *
     * @param sz The desired board size.
     * @throws IllegalStateException If it is called more than once in a single program execution.
     */
    public static void setSize(int sz) {
        if (sz != -1) {
            throw new IllegalStateException("setSize() called when size is already set.");
        }
        else {
            size = sz;
        }
    }

    /**
     * Get the size of the board. The size must already have been set.
     *
     * @return The size of the board.
     * @throws IllegalStateException If it is called before setSize() is called.
     */
    public int getSize() {
        if (size == -1) {
            throw new IllegalStateException("Size is not set yet.");
        }
        else {
            return size;
        }
    }

    public void placeStone(Stone stone, int row, int col) {
        // stub for now
    }

    public boolean isValidMove(Stone stone, int row, int col) {
        // stub for now
        return false;
    }

    public void flipAt(int row, int col) {
        // stub for now
    }

    public void finish() {
        // stub for now
    }

    public SquarePanel getAdjacent(Direction direction) {
        // stub for now
        return new SquarePanel();
    }
}
