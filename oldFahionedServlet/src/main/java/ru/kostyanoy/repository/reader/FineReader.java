package ru.kostyanoy.repository.reader;

import ru.kostyanoy.entity.Fine;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class FineReader {

    private FineReader() {
    }

    public static Fine readFine(ResultSet rs) throws SQLException {
        Fine fine = new Fine();
        fine.setId(rs.getLong("fine_id"));
        fine.setType (rs.getString("fine_type"));
        fine.setCharge(rs.getBigDecimal("fine_charge"));
        return fine;
    }
}
