package ru.kostyanoy.repository.reader;

import ru.kostyanoy.entity.CarOwner;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CarOwnerReader {

    private CarOwnerReader() {
    }

    public static CarOwner readCarOwner(ResultSet rs) throws SQLException {
        CarOwner carOwner = new CarOwner();
        carOwner.setId(rs.getLong("carOwner_id"));
        carOwner.setFirstName(rs.getString("carOwner_firstName"));
        carOwner.setMiddleName(rs.getString("carOwner_middleName"));
        carOwner.setLastName(rs.getString("carOwner_lastName"));
        return carOwner;
    }
}
