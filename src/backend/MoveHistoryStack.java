package backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to record the history of moves made.
 * Uses LIFO stack structure, but also gives ability to move back and forth without removing content.
 * Adding a new element (by making a move) will clear all histories after.
 * Note that the state before the first move is not recorded, so the first element in this stack
 * records the second turn of the game and so on.
 */
public class MoveHistoryStack {
    /**
     * Contains all move histories.
     */
    private List<MoveHistory> histories;
    /**
     * Records where in the stack is being viewed.
     * Should be equal to {@code histories.size() - 1} if looking
     * at the latest move.
     */
    private int currentPosition;

    /**
     * Instantiates the record of the history of moves made.
     * Call it after making the first move.
     *
     * @param stone   The stone placed in the move.
     * @param row     The row (or y-coordinate) of where the stone was placed. Start from 0.
     * @param col     The column (or x-coordinate) of where the stone was placed. Start from 0.
     * @param flipped The coordinates (array of 2 integers as [row, col]) of where the stones
     *                were flipped from this move.
     */
    public MoveHistoryStack(Stone stone, int row, int col, int[][] flipped) {
        this.histories = new ArrayList<>();
        this.histories.add(new MoveHistory(stone, row, col, flipped));
        this.currentPosition = 0;
    }

    /**
     * Adds a movement to the history.
     * If the current position isn't the end of the history, the moves after
     * the move being looked at will be removed.
     *
     * @param stone   The stone placed in the move.
     * @param row     The row (or y-coordinate) of where the stone was placed. Start from 0.
     * @param col     The column (or x-coordinate) of where the stone was placed. Start from 0.
     * @param flipped The coordinates (array of 2 integers as [row, col]) of where the stones
     *                were flipped from this move.
     */
    public void push(Stone stone, int row, int col, int[][] flipped) {
        if (this.currentPosition != this.histories.size()) {
            this.histories.subList(this.currentPosition + 1, this.histories.size()).clear();
        }
        this.histories.add(new MoveHistory(stone, row, col, flipped));
        this.currentPosition++;
    }

    /**
     * Returns the move before the current move.
     * This also shifts the current position
     * unless it is the first move.
     *
     * @return The move before the current move. {@code null} if it is the first move.
     */
    public MoveHistory previous() {
        if (this.currentPosition > 0) {
            this.currentPosition--;
            return this.histories.get(this.currentPosition);
        }
        return null;
    }

    /**
     * Returns the move after the current move.
     * This also shifts the current position
     * unless it is the last move.
     *
     * @return The move after the current move. {@code null} if it is the last move.
     */
    public MoveHistory next() {
        if (this.currentPosition < this.histories.size() - 1) {
            this.currentPosition++;
            return this.histories.get(this.currentPosition);
        }
        return null;
    }

    /**
     * Returns the move currently being viewed.
     *
     * @return The move currently being viewed.
     */
    public MoveHistory current() {
        return this.histories.get(this.currentPosition);
    }

    /**
     * Returns the number of move histories in the stack.
     *
     * @return The size of the stack.
     */
    public int size() {
        return this.histories.size();
    }

    /**
     * Returns the current position in the stack.
     *
     * @return The current position.
     */
    public int currentPosition() {
        return this.currentPosition;
    }
}
