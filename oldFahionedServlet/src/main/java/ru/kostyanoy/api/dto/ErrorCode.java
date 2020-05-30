package ru.kostyanoy.api.dto;

public enum ErrorCode {
    INTERNAL_ERROR(1),
    INVALID_PARAMETERS(2),
    SERVICE_UNAVAILABLE(3),
    OBJECT_NOT_FOUND(4);

    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
