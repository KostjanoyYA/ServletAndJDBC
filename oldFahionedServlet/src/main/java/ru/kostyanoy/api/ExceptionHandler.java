package ru.kostyanoy.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kostyanoy.api.dto.ErrorCode;
import ru.kostyanoy.api.dto.ErrorDto;
import ru.kostyanoy.exception.InternalErrorException;
import ru.kostyanoy.exception.InvalidParametersException;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.exception.ServiceUnavailableException;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class ExceptionHandler {

    private static final ExceptionHandler INSTANCE = new ExceptionHandler();

    private final ObjectMapper mapper = new ObjectMapper();

    private ExceptionHandler() {
    }

    static ExceptionHandler getInstance() {
        return INSTANCE;
    }

    void handleExceptions(Exception e, HttpServletResponse resp) throws IOException {
        resp.reset();
        resp.setContentType("application/json");
        resp.setStatus(getHttpCode(e));
        mapper.writeValue(resp.getOutputStream(), getError(e));
    }

    private ErrorDto getError(Exception e) {
        if (e instanceof InternalErrorException) {
            return new ErrorDto(ErrorCode.INTERNAL_ERROR, e.getMessage());
        }
        if (e instanceof InvalidParametersException) {
            return new ErrorDto(ErrorCode.INVALID_PARAMETERS, e.getMessage());
        }
        if (e instanceof ServiceUnavailableException) {
            return new ErrorDto(ErrorCode.SERVICE_UNAVAILABLE, e.getMessage());
        }
        if (e instanceof ObjectNotFoundException) {
            return new ErrorDto(ErrorCode.OBJECT_NOT_FOUND, e.getMessage());
        }
        return new ErrorDto(ErrorCode.INTERNAL_ERROR, "Unexpected error");
    }

    private int getHttpCode(Exception e) {
        if (e instanceof InternalErrorException) {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        if (e instanceof InvalidParametersException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }
        if (e instanceof ServiceUnavailableException) {
            return HttpServletResponse.SC_SERVICE_UNAVAILABLE;
        }
        if (e instanceof ObjectNotFoundException) {
            return HttpServletResponse.SC_NOT_FOUND;
        }
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}
