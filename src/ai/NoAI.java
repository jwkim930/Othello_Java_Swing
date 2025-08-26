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
     * Finds a move for this AI player.
     * For NoAI, this always returns null since it doesn't make moves.
     *
     * @return Always null, as NoAI doesn't make moves.
     */
    @Override
    public int[] findMove() {
        return null;
    }
}
