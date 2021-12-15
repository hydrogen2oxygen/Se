package net.hydrogen2oxygen.se.exceptions;

public class ParallelExecutionException extends Exception {

    public ParallelExecutionException(String message) {
        super(message);
    }

    public ParallelExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
