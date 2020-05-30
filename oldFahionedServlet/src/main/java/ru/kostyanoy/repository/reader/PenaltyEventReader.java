package ru.kostyanoy.repository.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kostyanoy.entity.PenaltyEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PenaltyEventReader {

    private static final Logger log = LoggerFactory.getLogger(PenaltyEventReader.class);

    private PenaltyEventReader() {
    }

    public static PenaltyEvent readPenaltyEvent(ResultSet rs) throws SQLException {
        PenaltyEvent penaltyEvent = new PenaltyEvent();
        penaltyEvent.setId(rs.getLong("penaltyEvent_id"));
        penaltyEvent.setTimeStamp(rs.getTimestamp("penaltyEvent_eventDate").toLocalDateTime());
        penaltyEvent.setCar(CarReader.readCar(rs));
        penaltyEvent.setFine(FineReader.readFine(rs));
        return penaltyEvent;
    }
}
