package ai;

import backend.Board;
import entities.Stone;
import gui.GameFrame;

import java.util.concurrent.CompletableFuture;

public abstract class AIPlayer {
    /**
     * The stone this AI should use.
     */
    Stone stone;

    /**
     * Instantiates the AI player.
     *
     * @param stone The stone the AI should use.
     */
    public AIPlayer(Stone stone) {
        this.stone = stone;
    }

    /**
     * The AI player tries to make a move.
     * It will skip to the next turn if it cannot make a move.
     * Can be called when it's not the AI's turn, in which case
     * this has no effect.
     */
    public void makeMove() {
        Board board = Board.getInstance();
        if (!board.getTurn().equals(this.stone)) {
            return;
        }

        board.toggleInteractable();

        // Run findMove() in a background task
        CompletableFuture<int[]> finderFuture = CompletableFuture.supplyAsync(this::findMove);

        // Run a 1-second timer
        CompletableFuture<Void> timerFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Continue when both finder and timer are completed
        CompletableFuture.allOf(finderFuture, timerFuture).thenRun(() -> {
            int[] move = finderFuture.join();

            if (move != null) {
                board.placeStone(move[0], move[1]);
            } else {
                GameFrame.getInstance().nextTurn();
            }

            board.toggleInteractable();
        });
    }

    /**
     * Finds the best move for this AI player.
     * This method should analyze the board and return the coordinate
     * of the square where the AI wants to place its stone.
     *
     * @return An array [row, col] representing the coordinate
     *         of the square to place the stone, or {@code null} if no valid move exists.
     */
    public abstract int[] findMove();
}
