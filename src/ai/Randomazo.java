package ai;

import backend.Board;
import entities.Stone;
import gui.DebugFrame;


/**
 * He will essentially press every square available until it makes a move.
 * If he does a good job, is it because of the player's luck, or Mr. Randomazo's?
 */
public class Randomazo extends AIPlayer {
    /**
     * Invite Mr. Randomazo to play. He will always make a random move.
     *
     * @param stone The stone Mr. Randomazo should use.
     */
    public Randomazo(Stone stone) {
        super(stone);
    }
    /**
     * The AI player tries to make a move.
     * It should skip to the next turn if it cannot make a move.
     * Can be called when it's not the AI's turn, in which case
     * this has no effect.
     *
     * For Mr. Randomazo, this is always a random move.
     */
    public void makeMove() {
        Board board = Board.getInstance();
        Thread worker = new Thread(() -> {
            board.toggleInteractable();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!DebugFrame.makeRandomMove()) {
                // couldn't make a move, skip manually
                board.nextTurn();
            }
            board.toggleInteractable();
        });
        if (board.getTurn().equals(this.stone)) {
            worker.start();
        }
    }
}
