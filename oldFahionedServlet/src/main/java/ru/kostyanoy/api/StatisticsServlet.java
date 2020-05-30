package ru.kostyanoy.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kostyanoy.service.statistics.DefaultStatisticsService;
import ru.kostyanoy.service.statistics.StatisticsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.kostyanoy.api.PathParser.getPathPart;

@WebServlet(urlPatterns = "/statistics/*")
public class StatisticsServlet extends HttpServlet {

    private static final String STATISTICS_PATTERN = "^/statistics/(?<top>[0-9]+)$";

    private final ObjectMapper mapper = new ObjectMapper();

    private final StatisticsService statisticsService = DefaultStatisticsService.getInstance();

    private final ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String path = getPath(req);
            if (path.matches(STATISTICS_PATTERN)) {
                getByTop(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            exceptionHandler.handleExceptions(e, resp);
        }
    }

    private void getByTop(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long top = getPathPart(getPath(req), STATISTICS_PATTERN, "top");

        List<ru.kostyanoy.api.dto.StatisticsDto> response = statisticsService.getByTop(top);
        writeResp(resp, response);
    }

    private String getPath(HttpServletRequest req) {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    private void writeResp(HttpServletResponse resp, Object response) throws IOException {
        resp.setContentType("application/json");
        mapper.writeValue(resp.getOutputStream(), response);
    }
}
