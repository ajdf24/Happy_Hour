package it.rieger.happyhour.model.exception;

/**
 * Created by sebastian on 31.12.16.
 */

public class NotSavedException extends RuntimeException {

    public NotSavedException() {
    }

    public NotSavedException(String message) {
        super(message);
    }

    public NotSavedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSavedException(Throwable cause) {
        super(cause);
    }
}