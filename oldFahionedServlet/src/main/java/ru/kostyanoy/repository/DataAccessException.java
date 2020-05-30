package ru.kostyanoy.repository;

public class DataAccessException extends RuntimeException {

    public DataAccessException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
