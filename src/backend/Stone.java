package backend;

import java.awt.*;

/**
 * Represents a stone. Can be either black or white.
 * Use the constants to access each.
 */
public class Stone {
    /**
     * Represents the color of the stone.
     * A positive number is white, negative is black.
     * It should never be 0.
     */
    private int color;
    /** A white stone */
    public static final Stone WHITE = new Stone(1);
    /** A black stone */
    public static final Stone BLACK = new Stone(-1);

    /**
     * Creates a stone of given color.
     *
     * @param color Positive if white, negative if black.
     * @throws IllegalArgumentException If color is 0.
     */
    private Stone(int color) {
        if (color == 0) {
            throw new IllegalArgumentException("The color of a stone cannot be 0.");
        }
        else {
            this.color = color;
        }
    }

    /**
     * Return a stone of the opposite color.
     *
     * @return A stone of the opposite color.
     */
    public Stone getOpposite() {
        return new Stone(-this.color);
    }

    /**
     * Returns the color of the stone.
     *
     * @return The color of the stone.
     */
    public Color getColor() {
        if (this.color > 0) {
            return Color.WHITE;
        }
        else if (this.color < 0) {
            return Color.BLACK;
        }
        else {
            throw new IllegalStateException("Stone color is invalid");
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * If both are Stones and they have the same color, it returns true.
     * Otherwise, it returns false.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Stone) {
            Stone s = (Stone) obj;
            return this.color / Math.abs(this.color) == s.color / Math.abs(s.color);
        }
        else {
            return false;
        }
    }
}
