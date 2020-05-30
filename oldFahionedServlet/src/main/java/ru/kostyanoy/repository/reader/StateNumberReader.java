package ru.kostyanoy.repository.reader;

import ru.kostyanoy.entity.StateNumber;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class StateNumberReader {

    private StateNumberReader() {
    }

    public static StateNumber readStateNumber(ResultSet rs) throws SQLException {
        StateNumber stateNumber = new StateNumber();
        stateNumber.setId(rs.getLong("stateNumber_id"));
        stateNumber.setNumber(rs.getInt("stateNumber_number"));
        stateNumber.setSeries(rs.getString("stateNumber_series"));
        stateNumber.setRegionCode(rs.getInt("stateNumber_regionCode"));
        stateNumber.setCountry(rs.getString("stateNumber_country"));
        return stateNumber;
    }
}
