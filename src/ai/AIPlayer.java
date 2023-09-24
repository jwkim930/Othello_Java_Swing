package ai;

import entities.Stone;

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
     * It should skip to the next turn if it cannot make a move.
     * Can be called when it's not the AI's turn, in which case
     * this has no effect.
     */
    public abstract void makeMove();
}
