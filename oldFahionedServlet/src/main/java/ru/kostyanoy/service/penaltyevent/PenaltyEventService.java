package ru.kostyanoy.service.penaltyevent;

import java.util.List;

public interface PenaltyEventService {

    ru.kostyanoy.api.dto.PenaltyEventDto create(ru.kostyanoy.api.dto.PenaltyEventDto penaltyEventDto);

    ru.kostyanoy.api.dto.PenaltyEventDto getById(Long id);

    List<ru.kostyanoy.api.dto.ReportDto> get(String firstName, String middleName, String lastName, String fullStateNumber);

    ru.kostyanoy.api.dto.PenaltyEventDto merge(Long id, ru.kostyanoy.api.dto.PenaltyEventDto PenaltyEventDto);

    void delete(Long id);
}
