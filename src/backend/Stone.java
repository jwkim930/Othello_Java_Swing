package backend;

/**
 * Represents a stone. Can be either black or white.
 * Use the constants to access each.
 */
public class Stone {
    /**
     * Represents the color of the stone.
     * 1 is white, -1 is black.
     */
    private int color;
    /** A white stone */
    public static final Stone WHITE = new Stone(1);
    /** A black stone */
    public static final Stone BLACK = new Stone(-1);

    /**
     * Creates a stone of given color.
     *
     * @param color 1 if white, -1 if black.
     */
    private Stone(int color) {
        this.color = color;
    }

    /**
     * Return a stone of the opposite color.
     *
     * @return A stone of the opposite color.
     */
    public Stone getOpposite() {
        return new Stone(-this.color);
    }
}
