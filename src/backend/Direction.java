package backend;

public class Direction implements Comparable<Direction> {
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
     * backend.Direction object representing the respective direction.
     */
    public static final Direction TOP = new Direction(1);
    /**
     * backend.Direction object representing the respective direction.
     */
    public static final Direction TOP_RIGHT = new Direction(2);
    /**
     * backend.Direction object representing the respective direction.
     */
    public static final Direction RIGHT = new Direction(3);
    /**
     * backend.Direction object representing the respective direction.
     */
    public static final Direction BOTTOM_RIGHT = new Direction(4);
    /**
     * backend.Direction object representing the respective direction.
     */
    public static final Direction BOTTOM = new Direction(5);
    /**
     * backend.Direction object representing the respective direction.
     */
    public static final Direction BOTTOM_LEFT = new Direction(6);
    /**
     * backend.Direction object representing the respective direction.
     */
    public static final Direction LEFT = new Direction(7);
    /**
     * backend.Direction object representing the respective direction.
     */
    public static final Direction TOP_LEFT = new Direction(8);

    /**
     * Creates a backend.Direction object with the specified direction.
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
     * That is, calling it on "Top" direction gives a "Top-right" direction.
     *
     * @return The new direction as indicated above.
     */
    public Direction clockwise() {
        return new Direction((this.tracker + 1) % 8);   // modulo for overflow
    }

    /**
     * Returns the direction that points to 45 degrees counter-clockwise from the current direction.
     * That is, calling it on "Top" direction gives a "Top-left" direction.
     *
     * @return The new direction as indicated above.
     */
    public Direction counterClockwise() {
        int newTracker = this.tracker > 1 ? this.tracker - 1 : 8;   // for overflow
        return new Direction(newTracker);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * The top direction is the "least", whereas top-left is the "greatest".
     * The "size" increases clockwise.
     *
     * @param dir the backend.Direction object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Direction dir) {
        return Integer.compare(this.tracker, dir.tracker);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * A backend.Direction object can only be equal to another backend.Direction object.
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
}
