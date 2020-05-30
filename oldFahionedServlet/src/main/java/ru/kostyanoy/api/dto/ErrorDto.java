package ru.kostyanoy.api.dto;

import java.util.Objects;

public class ErrorDto {

    private final int code;

    private final String message;

    public ErrorDto(ErrorCode code, String message) {
        this.code = code.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDto errorDto = (ErrorDto) o;
        return code == errorDto.code &&
                Objects.equals(message, errorDto.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
