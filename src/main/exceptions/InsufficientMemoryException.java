package main.exceptions;

public class InsufficientMemoryException extends Exception {
    public InsufficientMemoryException(String message) {
        super(message);
    }
}
