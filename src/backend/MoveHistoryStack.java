package backend;

import entities.Stone;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to record the history of moves made.
 * Uses LIFO stack structure, but also gives ability to move back and forth without removing content.
 * Adding a new element (by making a move) will clear all histories after.
 * Note that this stack records the changes, not the state of the board.
 */
public class MoveHistoryStack {
    /**
     * Contains all move histories.
     */
    private List<MoveHistory> histories;
    /**
     * Records where in the stack is being viewed.
     * Should be equal to {@code histories.size() - 1} if looking
     * at the latest move. Can be -1, which would indicate that
     * we're looking at the initial state.
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
        if (!this.atLastMove()) {
            this.histories.subList(this.currentPosition + 1, this.size()).clear();
        }
        this.histories.add(new MoveHistory(stone, row, col, flipped));
        this.currentPosition++;
    }

    /**
     * Returns the current move, then moves the current reading position backward.
     * This has no effect if we're currently at the state before the first move.
     * Note that the "current" move is the move that caused the current state of the board.
     * That is, it returns the move that, when applied in reverse, will restore the state
     * of the board one move ago.
     *
     * @return The current move. {@code null} if the current position is before the first move.
     */
    public MoveHistory previous() {
        MoveHistory move = this.current();
        if (!this.atBeforeFirstMove()) {
            this.currentPosition--;
        }
        return move;
    }

    /**
     * Returns the move after the current move and shifts the reading position forward.
     * This has no effect if we're currently at the last move.
     * Note that the "current" move is the move that caused the current state of the board.
     *
     * @return The move after the current move. {@code null} we're at the last move.
     */
    public MoveHistory next() {
        if (!this.atLastMove()) {
            this.currentPosition++;
            return this.histories.get(this.currentPosition);
        }
        return null;
    }

    /**
     * Returns the move after the current move.
     * Note that the "current" move is the move that caused the current state of the board.
     *
     * @return The move after the current move. {@code null} if it is the last move.
     */
    public MoveHistory peekNext() {
        if (!this.atLastMove()) {
            return this.histories.get(this.currentPosition + 1);
        }
        return null;
    }

    /**
     * Returns the move in the current position in the stack.
     * This is the move that caused the currently displayed state of the game.
     *
     * @return The move made right before the current state.
     *         {@code null} if looking at the state before the first move.
     */
    public MoveHistory current() {
        if (!atBeforeFirstMove()) {
            return this.histories.get(this.currentPosition);
        }
        return null;
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
    public int getCurrentPosition() {
        return this.currentPosition;
    }

    /**
     * Reports if the current reading position is at the last move in the stack.
     *
     * @return {@code true} if it is the last move, {@code false} otherwise.
     */
    public boolean atLastMove() {
        return this.currentPosition == this.size() - 1;
    }

    /**
     * Reports if the current reading position is at the one before the first move.
     *
     * @return {@code true} if it is before the first move, {@code false} otherwise.
     */
    public boolean atBeforeFirstMove() {
        return this.currentPosition == -1;
    }
}
