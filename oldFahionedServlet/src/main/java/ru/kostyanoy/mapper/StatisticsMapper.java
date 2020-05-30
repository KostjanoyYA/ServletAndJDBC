package ru.kostyanoy.mapper;

import ru.kostyanoy.entity.Statistics;

public class StatisticsMapper {

    private static final StatisticsMapper INSTANCE = new StatisticsMapper();

    private StatisticsMapper() {
    }

    public static StatisticsMapper getInstance() {
        return INSTANCE;
    }

    public ru.kostyanoy.api.dto.StatisticsDto toDto(Statistics statistics) {
        return ru.kostyanoy.api.dto.StatisticsDto.builder()
                .fineID(statistics.getFine().getId())
                .fineType(statistics.getFine().getType())
                .topPlace(statistics.getTopPlace())
                .occurrencesNumber(statistics.getOccurrencesNumber())
                .build();
    }
}
