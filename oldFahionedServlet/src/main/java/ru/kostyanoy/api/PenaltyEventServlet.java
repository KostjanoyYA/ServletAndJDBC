package ru.kostyanoy.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.kostyanoy.service.penaltyevent.DefaultPenaltyEventService;
import ru.kostyanoy.service.penaltyevent.PenaltyEventService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.kostyanoy.api.PathParser.getPathPart;

@WebServlet(urlPatterns = "/penaltyevents/*")
public class PenaltyEventServlet extends HttpServlet {

    private static final String PENALTY_EVENTS_PATTERN = "^/penaltyevents$";
    private static final String PENALTY_EVENT_PATTERN = "^/penaltyevents/(?<id>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();

    private final PenaltyEventService penaltyEventService = DefaultPenaltyEventService.getInstance();

    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    public PenaltyEventServlet() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PENALTY_EVENTS_PATTERN)) {
                get(req, resp);
            } else if (path.matches(PENALTY_EVENT_PATTERN)) {
                getById(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PENALTY_EVENTS_PATTERN)) {
                create(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PENALTY_EVENT_PATTERN)) {
                merge(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(PENALTY_EVENT_PATTERN)) {
                delete(req);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ru.kostyanoy.api.dto.PenaltyEventDto request = mapper.readValue(req.getInputStream(),
                ru.kostyanoy.api.dto.PenaltyEventDto.class);
        ru.kostyanoy.api.dto.PenaltyEventDto response = penaltyEventService.create(request);
        writeResp(resp, response);
    }

    private void getById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getPathPart(getPath(req), PENALTY_EVENT_PATTERN, "id");
        ru.kostyanoy.api.dto.PenaltyEventDto response = penaltyEventService.getById(id);
        writeResp(resp, response);
    }

    private void get(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstName = req.getParameter("firstName");
        String middleName = req.getParameter("middleName");
        String lastName = req.getParameter("lastName");
        String fullStateNumber = req.getParameter("fullStateNumber");

        List<ru.kostyanoy.api.dto.ReportDto> response = penaltyEventService.get(firstName, middleName, lastName,
                fullStateNumber);
        writeResp(resp, response);
    }

    private void merge(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getPathPart(getPath(req), PENALTY_EVENT_PATTERN, "id");
        ru.kostyanoy.api.dto.PenaltyEventDto request;
        request = mapper.readValue(req.getInputStream(), ru.kostyanoy.api.dto.PenaltyEventDto.class);
        ru.kostyanoy.api.dto.PenaltyEventDto response = penaltyEventService.merge(id, request);
        writeResp(resp, response);
    }

    private void delete(HttpServletRequest req) {
        Long id = getPathPart(getPath(req), PENALTY_EVENT_PATTERN, "id");
        penaltyEventService.delete(id);
    }


    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
