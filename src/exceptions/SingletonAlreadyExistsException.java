package exceptions;

/**
 * A singleton instance of the class already exists.
 * This is an unchecked exception.
 */
public class SingletonAlreadyExistsException extends RuntimeException{
    /**
     * Default constructor.
     */
    public SingletonAlreadyExistsException() {
        super();
    }

    /**
     * Constructor that allows a specific detail message to appear.
     *
     * @param msg The detail message.
     */
    public SingletonAlreadyExistsException(String msg) {
        super(msg);
    }
}
