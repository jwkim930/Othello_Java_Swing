package backend;

import exceptions.SingletonAlreadyExistsException;
import exceptions.SingletonNotYetExistsException;
import gui.BoardPanel;
import gui.GameFrame;
import gui.SquarePanel;

import java.util.ArrayList;
import java.util.List;

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
     * The player of the current turn. Black takes the first turn, then it alternates.
     */
    private Stone turn;

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
                this.squares[row][col] = new SquarePanel(row, col);
            }
        }
        // place the 4 starting stones
        int topLeft = size / 2 - 1;
        this.getSquareAt(topLeft, topLeft).place(Stone.WHITE);
        this.getSquareAt(topLeft, topLeft + 1).place(Stone.BLACK);
        this.getSquareAt(topLeft + 1, topLeft).place(Stone.BLACK);
        this.getSquareAt(topLeft + 1, topLeft + 1).place(Stone.WHITE);
        // the first turn is black
        this.turn = Stone.BLACK;
    }

    /**
     * Initializes the board singleton instance.
     *
     * @param sz The size of the board.
     * @throws SingletonAlreadyExistsException If this has already been called before.
     */
    public static void initialize(int sz) throws SingletonAlreadyExistsException {
        if (instance != null) {
            throw new SingletonAlreadyExistsException("initialize() called after Board has been initialized.");
        }
        else {
            instance = new Board(sz);
        }
    }

    /**
     * Removes the singleton instance of this object so that it can be initialized again.
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Returns the singleton instance of the board.
     *
     * @return The instance of the board.
     * @throws SingletonNotYetExistsException If the board has not been initialized yet.
     */
    public static Board getInstance() throws SingletonNotYetExistsException {
        if (instance == null) {
            throw new SingletonNotYetExistsException("getInstance() called before Board has been initialized.");
        }
        else {
            return instance;
        }
    }

    /**
     * Get the 2D array of square cells in the board.
     * Refrain from using this unless you need to access
     * the entire board, and use {@code getSquareAt()} instead.
     *
     * @return The array of square cells in the board.
     */
    public SquarePanel[][] getSquares() {
        return this.squares;
    }

    /**
     * Return the SquarePanel at the coordinate. Returns {@code null} if the
     * given coordinate is outside the board.
     *
     * @param row The row of the coordinate.
     * @param col The column of the coordinate.
     * @return The SquarePanel at the coordinate. {@code null} if the coordinate is
     * outside the board.
     */
    public SquarePanel getSquareAt(int row, int col) {
        try {
            return this.squares[row][col];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
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
     * Attempts to place the stone for the current turn on the given coordinate.
     * If it is a valid move, the stone is placed and the appropriate stones are flipped,
     * then the other player takes turn. If it is not a valid move, nothing happens on the board.
     *
     * @param row The row that the square to place the stone on is in.
     * @param col The column that the square to place the stone on is in.
     * @return {@code true} if it's a valid move, {@code false} otherwise.
     */
    public boolean placeStone(int row, int col) {
        Stone stone = this.getTurn();
        Direction[] flippableDirections = this.getFlippingDirections(stone, row, col);
        if (flippableDirections.length == 0) {
            return false;
        }
        else {
            // place stone and flip appropriately
            this.getSquareAt(row, col).place(stone);
            for (Direction dir : flippableDirections) {
                // getFlippingDirections() already ensures all stones in the direction are flippable
                for (SquarePanel square = this.getSquareAt(row, col).getAdjacent(dir);
                  square.getStone().equals(stone.getOpposite());
                  square = square.getAdjacent(dir)) {
                    square.flip();
                }
            }
            GameFrame.getInstance().nextTurn();
            return true;
        }
    }

    /**
     * Returns all directions where there would be at least one stone
     * that will be flipped upon placing the stone on the square.
     *
     * @param stone The stone to be placed.
     * @param row The row of the square to place the stone at.
     * @param col The column of the square to place the stone at.
     * @return The directions where there will be stones flipped. May be empty.
     */
    public Direction[] getFlippingDirections(Stone stone, int row, int col) {
        List<Direction> resultList = new ArrayList<>();
        Direction dir = Direction.TOP;
        do {
            boolean addThis = false;
            SquarePanel square = this.getSquareAt(row, col).getAdjacent(dir);
            // must be within the board and not empty
            // must have at least one stone of opposite color, then one of same color
            boolean oppositeSeen = false;
            while (square != null && square.getStone() != null) {
                Stone stoneAtSquare = square.getStone();
                if (!oppositeSeen && stone.equals(stoneAtSquare.getOpposite())) {
                    // first time seeing the opposite stone; must occur in the first iteration
                    oppositeSeen = true;
                }
                else if (!oppositeSeen && stone.equals(stoneAtSquare)) {
                    // saw the same stone without seeing an opposite stone; must occur in the first iteration
                    break;
                }
                else if (oppositeSeen && stone.equals(stoneAtSquare)) {
                    // saw the same stone after seeing opposite stone(s)
                    addThis = true;
                    break;
                }
                square = square.getAdjacent(dir);
            }
            if (addThis) {
                resultList.add(dir);
            }
            dir = dir.clockwise();
        } while (!dir.equals(Direction.TOP));

        // put them in an array
        Direction[] result = new Direction[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        return result;
    }

    /**
     * Returns the stone of the player of the current turn.
     *
     * @return The stone to be placed this turn.
     */
    public Stone getTurn() {
        return this.turn;
    }

    /**
     * Let the other player make the move.
     * Should only be called by GameFrame, otherwise call nextTurn() in GameFrame.
     */
    public void nextTurn() {
        this.turn = this.turn.getOpposite();
    }
}
