package backend;

import entities.Direction;
import entities.Stone;
import exceptions.SingletonNotYetExistsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
     * Private constructor for parsing from a text file.
     *
     * @param path The path to the text file.
     */
    private MockBoard(String path) {
        try (Scanner scanner = new Scanner(new File(path))) {
            String turn = scanner.nextLine();
            if (turn.equals("B")) {
                this.turn = Stone.BLACK;
            }
            else {
                this.turn = Stone.WHITE;
            }
            int size = Integer.parseInt(scanner.nextLine());
            this.squares = new Stone[size][size];
            for (int row = 0; row < size; row++) {
                String line = scanner.nextLine();
                for (int col = 0; col < size; col++) {
                    char at = line.charAt(col);
                    if (at == 'B') {
                        this.squares[row][col] = Stone.BLACK;
                    }
                    else if (at == 'W') {
                        this.squares[row][col] = Stone.WHITE;
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MockBoard other) {
            return Arrays.deepEquals(this.squares, other.squares) && this.turn.equals(other.turn);
        }
        return false;
    }

    /**
     * Returns the string representation of this object.
     * A black stone is marked with B, a white stone is marked with W,
     * and an empty square is marked with *.
     *
     * @return The string representation of this MockBoard.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < this.getSize(); row++) {
            for (int col = 0; col < this.getSize(); col++) {
                String squareString = "*";
                if (Stone.BLACK.equals(this.getStoneAt(row, col))) {
                    squareString = "B";
                }
                else if (Stone.WHITE.equals(this.getStoneAt(row, col))) {
                    squareString = "W";
                }
                result.append(squareString);
            }
            result.append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
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

    /**
     * Returns all moves that will flip at least one stone.
     *
     * @return The moves that will flip at least one stone.
     *         Feel free to modify this list since it's not used anywhere else.
     */
    public List<int[]> getValidMoves() {
        List<int[]> validMoves = new ArrayList<>();
        // just try every empty square
        for (int row = 0; row < this.getSize(); row++) {
            for (int col = 0; col < this.getSize(); col++) {
                if (this.getStoneAt(row, col) == null && this.getFlippingDirections(this.turn, row, col).length > 0) {
                    validMoves.add(new int[] {row, col});
                }
            }
        }
        return validMoves;
    }

    /**
     * Count the number of a particular stone on the board.
     *
     * @param stone The stone to be counted.
     * @return The number of that stone.
     */
    public int countStones(Stone stone) {
        int count = 0;
        for (int row = 0; row < this.getSize(); row++) {
            for (int col = 0; col < this.getSize(); col++) {
                if (stone.equals(this.squares[row][col])) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Create the object instance from a text file.
     * Refer to the {@code DebugFrame.saveBoardState}
     * documentation for file formatting.
     *
     * @param path The path to the text file.
     * @return The {@code MockBoard} instance copying
     * the state in the file.
     */
    public static MockBoard parse(String path) {
        return new MockBoard(path);
    }
}
