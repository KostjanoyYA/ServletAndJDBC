package ru.kostyanoy.repository.car;

import ru.kostyanoy.entity.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository {

    void add(Car car);

    Optional<Car> getById(Long id);

    List<Car> getByUserId(Long userId);

    List<Car> get(String make, String model);

    Optional<Car> getByStateNumberId(Long stateNumberID);

    void update(Car car);

    void delete(Car car);
}
