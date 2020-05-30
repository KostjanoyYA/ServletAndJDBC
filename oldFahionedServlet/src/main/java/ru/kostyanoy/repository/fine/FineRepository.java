package ru.kostyanoy.repository.fine;

import ru.kostyanoy.entity.Fine;

import java.util.List;
import java.util.Optional;

public interface FineRepository {

    Optional<Fine> getById(Long id);

    List<Fine> get();
}
