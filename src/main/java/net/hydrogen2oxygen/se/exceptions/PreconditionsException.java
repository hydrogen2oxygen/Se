package net.hydrogen2oxygen.se.exceptions;

public class PreconditionsException extends Exception {

    public PreconditionsException(String message) {
        super(message);
    }

    public PreconditionsException(String message, Throwable e) {
        super(message,e);
    }
}
