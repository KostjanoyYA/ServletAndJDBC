package ru.kostyanoy.repository.carowner;

import ru.kostyanoy.entity.CarOwner;

import java.util.List;
import java.util.Optional;

public interface CarOwnerRepository {

    void add(CarOwner carOwner);

    Optional<CarOwner> getById(Long id);

    List<CarOwner> get(String... fromLastToFirstNames);

    void update(CarOwner carOwner);
}
