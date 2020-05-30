package ru.kostyanoy.service.statistics;

import java.util.List;

public interface StatisticsService {

    List<ru.kostyanoy.api.dto.StatisticsDto> getByTop(Long top);
}
