package exceptions;

/**
 * A singleton instance of the class does not exist yet.
 * This is an unchecked exception.
 */
public class SingletonNotYetExistsException extends RuntimeException {
    /**
     * Default constructor.
     */
    public SingletonNotYetExistsException() {
        super();
    }

    /**
     * Constructor that allows a specific detail message to appear.
     *
     * @param msg The detail message.
     */
    public SingletonNotYetExistsException(String msg) {
        super(msg);
    }
}
