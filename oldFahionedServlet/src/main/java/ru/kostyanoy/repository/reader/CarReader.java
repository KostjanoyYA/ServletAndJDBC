package ru.kostyanoy.repository.reader;

import ru.kostyanoy.entity.Car;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CarReader {

    private CarReader() {
    }

    public static Car readCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("car_id"));
        car.setMake(rs.getString("car_make"));
        car.setModel(rs.getString("car_model"));
        car.setStateNumber(StateNumberReader.readStateNumber(rs));
        car.setCarOwner(CarOwnerReader.readCarOwner(rs));
        return car;
    }
}
