package ru.kostyanoy.repository.penaltyevent;

import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.StateNumber;

import java.util.List;
import java.util.Optional;

public interface PenaltyEventRepository {

    void add(PenaltyEvent penaltyEvent);

    Optional<PenaltyEvent> getById(Long id);

    List<PenaltyEvent> get(String firstName, String middleName, String lastName);

    List<PenaltyEvent> get(StateNumber stateNumber);

    void update(PenaltyEvent penaltyEvent);

    void delete(PenaltyEvent penaltyEvent);
}
