package ru.kostyanoy.repository.statistics;

import ru.kostyanoy.entity.Statistics;

import java.util.List;

public interface StatisticsRepository {

    List<Statistics> getByTop(Long top);
}
