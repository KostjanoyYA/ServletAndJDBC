package ru.kostyanoy.exception;

public class InvalidParametersException extends ApplicationException {

    public InvalidParametersException(String message) {
        super(message);
    }

    public InvalidParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}
