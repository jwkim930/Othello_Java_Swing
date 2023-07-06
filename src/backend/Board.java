package backend;

import gui.BoardPanel;
import gui.SquarePanel;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
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
        this.placeStone(Stone.WHITE, topLeft, topLeft);
        this.placeStone(Stone.BLACK, topLeft, topLeft + 1);
        this.placeStone(Stone.BLACK, topLeft + 1, topLeft);
        this.placeStone(Stone.WHITE, topLeft + 1, topLeft + 1);
        // the first turn is black
        this.turn = Stone.BLACK;
    }

    /**
     * Initializes the board singleton instance.
     *
     * @param sz The size of the board.
     * @throws InstanceAlreadyExistsException If this has already been called before.
     */
    public static void initialize(int sz) throws InstanceAlreadyExistsException {
        if (instance != null) {
            throw new InstanceAlreadyExistsException("initialize() called after Board has been initialized.");
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
            throw new InstanceNotFoundException("getInstance() called before Board has been initialized.");
        }
        else {
            return instance;
        }
    }

    /**
     * Get the 2D array of square cells in the board.
     * Refrain from using this, and use getSquareAt() whenever possible.
     *
     * @return The array of square cells in the board.
     */
    public SquarePanel[][] getSquares() {
        return this.squares;
    }

    /**
     * Return the SquarePanel at the coordinate.
     *
     * @param row The row of the coordinate.
     * @param col The column of the coordinate.
     * @return The SquarePanel at the coordinate.
     */
    public SquarePanel getSquareAt(int row, int col) {
        return this.squares[row][col];
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
            int[] coor = dir.moveThisWay(row, col);
            // must be within the board and not empty
            // must have at least one stone of opposite color, then one of same color
            boolean oppositeSeen = false;
            while (coor != null && this.squares[coor[0]][coor[1]].getStone() != null) {
                Stone stoneAtSquare = this.squares[coor[0]][coor[1]].getStone();
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
                coor = dir.moveThisWay(coor[0], coor[1]);
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
     * Flips the stone at the coordinate.
     *
     * @param row The row of the square.
     * @param col The column of the square.
     */
    public void flipAt(int row, int col) {
        this.squares[row][col].flip();
    }

    public void finish() {
        // stub for now
    }

    /**
     * Returns the SquarePanel that is adjacent to a SquarePanel in the specified direction.
     *
     * @param row The row of the panel to start off of.
     * @param col The column of the panel to start off of.
     * @param dir The direction to move towards.
     * @return The adjacent SquarePanel in the direction, {@code null} if it is out of board.
     */
    public SquarePanel getAdjacent(int row, int col, Direction dir) {
        int[] result = dir.moveThisWay(row, col);
        if (result != null) {
            return this.squares[result[0]][result[1]];
        }
        else {
            return null;
        }
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
