package ru.kostyanoy.repository.reader;

import ru.kostyanoy.entity.Statistics;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class StatisticsReader {

    private StatisticsReader() {
    }

    public static Statistics readStatistics(ResultSet rs) throws SQLException {
        Statistics statistics = new Statistics();
        statistics.setFine(FineReader.readFine(rs));
        statistics.setTopPlace(rs.getLong("fine_top_place"));
        statistics.setOccurrencesNumber(rs.getLong("fine_occurrences"));
        return statistics;
    }
}
