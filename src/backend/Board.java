package backend;

import gui.BoardPanel;
import gui.SquarePanel;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

/**
 * Represents the game board. This class uses a singleton pattern.
 * Call initialize() in the beginning and use getInstance() to access it
 * afterwards.
 */
public class Board {
    /**
     * The singleton instance of this object.
     */
    private static Board instance;
    /**
     * Contains all squares in the board. Access by [row][col].
     */
    private SquarePanel[][] squares;
    /**
     * The size of the board. The board is square, so this is both width and height.
     */
    private int size;

    /**
     * Initializes the Othello board. Should only be called once in the beginning.
     *
     * @param size The size of the board.
     */
    private Board(int size) {
        this.size = size;
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
     * Initializes the board singleton instance.
     *
     * @param sz The size of the board.
     * @throws InstanceAlreadyExistsException If this has already been called before.
     */
    public static void initialize(int sz) throws InstanceAlreadyExistsException {
        if (instance != null) {
            throw new InstanceAlreadyExistsException();
        }
        else {
            instance = new Board(sz);
        }
    }

    /**
     * Returns the singleton instance of the board.
     *
     * @return The instance of the board.
     * @throws InstanceNotFoundException If the board has not been initialized yet.
     */
    public static Board getInstance() throws InstanceNotFoundException {
        if (instance == null) {
            throw new InstanceNotFoundException();
        }
        else {
            return instance;
        }
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
     * Get the size of the board.
     *
     * @return The size of the board.
     */
    public int getSize() {
        return this.size;
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
