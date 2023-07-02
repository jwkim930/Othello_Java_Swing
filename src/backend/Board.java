package backend;

import gui.BoardPanel;
import gui.GameFrame;
import gui.SquarePanel;

public class Board {
    /**
     * Contains all squares in the board. Access by [row][col].
     */
    private SquarePanel[][] squares;
    /**
     * The size of the board. The board is square, so this is both width and height.
     * When the board is not initialized, this is -1.
     * Made static so that the static method setSize() can call it.
     */
    private static int size = -1;

    /**
     * Initializes the Othello board. Should only be called once in the beginning.
     */
    public Board() {
        int size = getSize();
        this.squares = new SquarePanel[size][size];
        int sqSize = BoardPanel.SIZE / size;
        SquarePanel.setSquareSize(sqSize);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.squares[row][col] = new SquarePanel();
            }
        }
        // place the 4 starting stones
        int topLeft = size / 2 - 1;
        this.placeStone(Stone.WHITE, topLeft, topLeft);
        this.placeStone(Stone.BLACK, topLeft, topLeft + 1);
        this.placeStone(Stone.BLACK, topLeft + 1, topLeft);
        this.placeStone(Stone.WHITE, topLeft + 1, topLeft + 1);
    }

    /**
     * Get the 2D array of square cells in the board.
     *
     * @return The array of square cells in the board.
     */
    public SquarePanel[][] getSquares() {
        return this.squares;
    }

    /**
     * Sets the board size. Can only be called once.
     * Static so that it can be called before calling the constructor.
     *
     * @param sz The desired board size.
     * @throws IllegalStateException If it is called more than once in a single program execution.
     */
    public static void setSize(int sz) {
        if (size != -1) {
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
    public static int getSize() {
        if (size == -1) {
            throw new IllegalStateException("Size is not set yet.");
        }
        else {
            return size;
        }
    }

    /**
     * Places the stone in the given coordinate.
     *
     * @param stone The stone to be placed.
     * @param row The row that the square to place the stone on is in.
     * @param col The column that the square to place the stone on is in.
     */
    public void placeStone(Stone stone, int row, int col) {
        this.squares[row][col].place(stone);
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
