package backend;

import javax.management.InstanceNotFoundException;

/**
 * Represents a direction. There are 8 directions in total.
 * Use the constants to access each.
 */
public class Direction {
    /**
     * The name of the direction. Access with toString().
     */
    private String name;
    /**
     * Represents a direction. Ranges from 1 to 8, where 1 is the top direction,
     * and it increases as it goes clockwise until reaching top-left (8).
     */
    private int tracker;
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction TOP = new Direction(1);
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction TOP_RIGHT = new Direction(2);
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction RIGHT = new Direction(3);
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction BOTTOM_RIGHT = new Direction(4);
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction BOTTOM = new Direction(5);
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction BOTTOM_LEFT = new Direction(6);
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction LEFT = new Direction(7);
    /**
     * Direction object representing the respective direction.
     */
    public static final Direction TOP_LEFT = new Direction(8);

    /**
     * Creates a Direction object with the specified direction.
     * Starting from the top, there are 8 directions that
     * proceed clockwise.
     *
     * @param dir represents a direction. 1 is top, 2 is top-right, etc..
     */
    private Direction(int dir) {
        this.tracker = dir;
        switch (dir) {
            case 1 -> this.name = "Top";
            case 2 -> this.name = "Top-right";
            case 3 -> this.name = "Right";
            case 4 -> this.name = "Bottom-right";
            case 5 -> this.name = "Bottom";
            case 6 -> this.name = "Bottom-left";
            case 7 -> this.name = "Left";
            case 8 -> this.name = "Top-left";
        }
    }

    /**
     * Returns the direction that points to 45 degrees clockwise from the current direction.
     * For example, calling it on "Top" direction gives a "Top-right" direction.
     *
     * @return The new direction as indicated above.
     */
    public Direction clockwise() {
        int newTracker = this.tracker < 8 ? this.tracker + 1 : 1;   // overflow if top-left
        return new Direction(newTracker);
    }

    /**
     * Returns the direction that points to 45 degrees counter-clockwise from the current direction.
     * For example, calling it on "Top" direction gives a "Top-left" direction.
     *
     * @return The new direction as indicated above.
     */
    public Direction counterClockwise() {
        int newTracker = this.tracker > 1 ? this.tracker - 1 : 8;   // overflow if top
        return new Direction(newTracker);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * A Direction object can only be equal to another Direction object.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Direction) {
            Direction dir = (Direction) obj;
            return this.tracker == dir.tracker;
        }
        else {
            return false;
        }
    }

    /**
     * Returns a string representation of the object.
     * This is the name of the direction (i.e. "Top", etc.).
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns the coordinate of the square you'd end up
     * by moving in the direction.
     *
     * @param startRow The row of the starting square.
     * @param startCol The column of the starting square.
     * @return The destination square. Contains 2 elements as [row, col].
     * {@code null} if out of the board.
     */
    public int[] moveThisWay(int startRow, int startCol) {
        int boardSize;
        try {
            boardSize = Board.getInstance().getSize();
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException(e);
        }
        switch (this.tracker) {
            case 1 -> startRow--;
            case 2 -> {startRow--; startCol++;}
            case 3 -> startCol++;
            case 4 -> {startRow++; startCol++;}
            case 5 -> startRow++;
            case 6 -> {startRow++; startCol--;}
            case 7 -> startCol--;
            case 8 -> {startRow--; startCol--;}
        }
        if (startRow >= 0 && startRow < boardSize && startCol >= 0 && startCol < boardSize) {
            return new int[] {startRow, startCol};
        }
        else {
            return null;
        }
    }
}
