package backend;

import entities.Direction;
import entities.Stone;
import exceptions.SingletonNotYetExistsException;

import java.util.ArrayList;
import java.util.List;

/**
 * A lightweight version of Board for AI simulations.
 * Does not use singleton pattern and stores {@code Stone} instances directly without {@code SquarePanel}.
 */
public class MockBoard {
    /**
     * Contains all instances of {@code Stone} in the board.
     * {@code null} represents an empty square.
     */
    private Stone[][] squares;

    /**
     * The player of the current turn.
     */
    private Stone turn;

    /**
     * Initializes the class based on the current state of the Board singleton.
     *
     * @throws SingletonNotYetExistsException If the Board has not been initialized yet.
     */
    public MockBoard() throws SingletonNotYetExistsException {
        Board board = Board.getInstance();
        int size = board.getSize();
        this.squares = new Stone[size][size];
        this.turn = board.getTurn();

        // Copy the current state from Board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.squares[row][col] = board.getSquareAt(row, col).getStone();
            }
        }
    }

    /**
     * Private constructor for creating copies.
     *
     * @param squares The 2D array of stones to copy.
     * @param turn The current turn.
     */
    private MockBoard(Stone[][] squares, Stone turn) {
        int size = squares.length;
        this.squares = new Stone[size][size];
        this.turn = turn;

        // Deep copy the squares array
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.squares[row][col] = squares[row][col];
            }
        }
    }

    /**
     * Attempts to place the current turn player's stone on the given coordinate.
     * If it is a valid move, the stone is placed and the appropriate stones are flipped,
     * then the other player takes turn. If it is not a valid move, nothing happens on the board.
     *
     * @param row The row that the square to place the stone on is in.
     * @param col The column that the square to place the stone on is in.
     * @return The number of stones flipped, or 0 if the move was illegal.
     */
    public int placeStone(int row, int col) {
        if (this.getStoneAt(row, col) != null) {
            // stone already exists on the square or coordinate is outside the board
            return 0;
        }

        Direction[] flippableDirections = this.getFlippingDirections(this.getTurn(), row, col);
        if (flippableDirections.length == 0) {
            // no stone would be flipped; invalid move
            return 0;
        }

        // place stone and flip appropriately
        this.squares[row][col] = this.getTurn();
        int flippedCount = 0;

        for (Direction dir : flippableDirections) {
            int[] pos = dir.moveThisWay(row, col);
            while (pos != null && this.getStoneAt(pos[0], pos[1]) != null
                    && getStoneAt(pos[0], pos[1]).equals(this.getTurn().getOpposite())) {
                this.squares[pos[0]][pos[1]] = this.getTurn();
                flippedCount++;
                pos = dir.moveThisWay(pos[0], pos[1]);
            }
        }

        // alternate the turn and return result
        this.nextTurn();
        return flippedCount;
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
            int[] pos = dir.moveThisWay(row, col);
            boolean oppositeSeen = false;

            while (pos != null && this.getStoneAt(pos[0], pos[1]) != null) {
                Stone stoneAtSquare = this.getStoneAt(pos[0], pos[1]);
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
                pos = dir.moveThisWay(pos[0], pos[1]);
            }

            if (addThis) {
                resultList.add(dir);
            }
            dir = dir.clockwise();
        } while (!dir.equals(Direction.TOP));

        return resultList.toArray(new Direction[0]);
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
     */
    public void nextTurn() {
        this.turn = this.turn.getOpposite();
    }

    /**
     * Returns a deep copy of this class instance.
     *
     * @return A deep copy of this instance.
     */
    public MockBoard copy() {
        return new MockBoard(this.squares, this.turn);
    }

    /**
     * Get the stone at the specified coordinate.
     *
     * @param row The row of the coordinate.
     * @param col The column of the coordinate.
     * @return The stone at the coordinate, or {@code null} if empty or out of bounds.
     */
    public Stone getStoneAt(int row, int col) {
        if (row < 0 || row >= squares.length || col < 0 || col >= squares.length) {
            return null;
        }
        return this.squares[row][col];
    }

    /**
     * Get the size of the board.
     *
     * @return The size of the board.
     */
    public int getSize() {
        return this.squares.length;
    }
}
