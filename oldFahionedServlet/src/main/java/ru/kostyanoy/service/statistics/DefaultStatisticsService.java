package ru.kostyanoy.service.statistics;

import ru.kostyanoy.exception.InvalidParametersException;
import ru.kostyanoy.mapper.StatisticsMapper;
import ru.kostyanoy.repository.statistics.JdbcStatisticsRepository;
import ru.kostyanoy.repository.statistics.StatisticsRepository;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultStatisticsService implements StatisticsService {

    private static final DefaultStatisticsService INSTANCE = new DefaultStatisticsService();

    private final StatisticsRepository statisticsRepository = JdbcStatisticsRepository.getInstance();

    private final StatisticsMapper statisticsMapper = StatisticsMapper.getInstance();

    private DefaultStatisticsService() {
    }

    public static StatisticsService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ru.kostyanoy.api.dto.StatisticsDto> getByTop(Long top) {
        if (top == null || top <= 0) {
            throw new InvalidParametersException("Can't calculate the statistics. Invalid top parameter");
        }
        return statisticsRepository.getByTop(top)
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());
    }
}
