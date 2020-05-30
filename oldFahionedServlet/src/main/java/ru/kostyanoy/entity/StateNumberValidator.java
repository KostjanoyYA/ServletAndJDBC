package ru.kostyanoy.entity;

import java.util.Optional;

public interface StateNumberValidator {
    Optional<StateNumber> parseStateNumber(String fullNumber);

    String generateFullNumber(StateNumber stateNumber);
}
