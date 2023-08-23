package backend;

/**
 * Records the stone placed and stones flipped in a move.
 */
public class MoveHistory {
    /**
     * The coordinate of where the stone was placed in this move.
     * Has 2 elements in the format [row, col] (starts from 0).
     */
    private int[] location;
    /**
     * The stone placed in this move.
     */
    private Stone stone;
    /**
     * The coordinates of the stones flipped from this move.
     * Each array has 2 elements in the format [row, col] (starts from 0).
     */
    private int[][] flipped;

    /**
     * Initializes a move history.
     *
     * @param stone   The stone placed in the move.
     * @param row     The row (or y-coordinate) of where the stone was placed. Start from 0.
     * @param col     The column (or x-coordinate) of where the stone was placed. Start from 0.
     * @param flipped The coordinates (array of 2 integers as [row, col]) of where the stones
     *                were flipped from this move.
     */
    public MoveHistory(Stone stone, int row, int col, int[][] flipped) {
        this.stone = stone;
        this.location = new int[] {row, col};
        this.flipped = flipped;
    }

    /**
     * Returns the coordinate of where the stone was placed.
     * The returned array is a copy, so modifying it does not affect the original array.
     *
     * @return The coordinate of where the stone was placed.
     */
    public int[] getLocation() {
        return this.location.clone();
    }

    /**
     * Returns the stone that was placed in this move.
     *
     * @return The stone placed in this move.
     */
    public Stone getStone() {
        return this.stone;
    }

    /**
     * Returns the coordinates of where stones were flipped.
     * The returned array is a deep copy, so modifying it does not affect the original array.
     *
     * @return The coordinates of where stones were flipped.
     */
    public int[][] getFlipped() {
        // make a deep copy
        int[][] output = new int[this.flipped.length][2];
        for (int i = 0; i < this.flipped.length; i++) {
            output[i] = this.flipped[i].clone();
        }

        return output;
    }
}
