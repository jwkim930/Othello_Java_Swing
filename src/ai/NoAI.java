package ai;

/**
 * Used when PvP option is used. Does not make a move on its own.
 */
public class NoAI extends AIPlayer {
    /**
     * Instantiates the AI player.
     * The stone will be {@code null}.
     */
    public NoAI() {
        super(null);
    }

    /**
     * The AI player tries to make a move.
     * It should skip to the next turn if it cannot make a move.
     * Can be called when it's not the AI's turn, in which case
     * this has no effect.
     * 
     * For this class, this has no effect.
     */
    public void makeMove() {}
}
