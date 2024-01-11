package main.exceptions;

/**
 * Exception thrown when there is insufficient memory to perform an operation.
 */
public class InsufficientMemoryException extends Exception {

    /**
     * Constructs an {@code InsufficientMemoryException} with the specified detail message.
     *
     * @param message The detail message.
     */
    public InsufficientMemoryException(String message) {
        super(message);
    }
}
