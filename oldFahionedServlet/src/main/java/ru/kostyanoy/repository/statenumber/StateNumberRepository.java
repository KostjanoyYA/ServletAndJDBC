package ru.kostyanoy.repository.statenumber;

import ru.kostyanoy.entity.StateNumber;

import java.util.Optional;

public interface StateNumberRepository {

    void add(StateNumber stateNumber);

    Optional<StateNumber> getById(Long id);

    Optional<StateNumber> get(StateNumber stateNumber);

    void update(StateNumber stateNumber);
}
