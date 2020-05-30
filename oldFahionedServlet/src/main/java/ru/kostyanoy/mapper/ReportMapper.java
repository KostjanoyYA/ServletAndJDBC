package ru.kostyanoy.mapper;

import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.StateNumberValidator;

import java.util.ArrayList;
import java.util.List;

public class ReportMapper {

    private static final ReportMapper INSTANCE = new ReportMapper();

    private ReportMapper() {
    }

    public static ReportMapper getInstance() {
        return INSTANCE;
    }

    public List<ru.kostyanoy.api.dto.ReportDto> toDto(List<PenaltyEvent> events, StateNumberValidator validator) { //TODO перенести validator
        List<ru.kostyanoy.api.dto.ReportDto> reportDtos = new ArrayList<>();
        for (PenaltyEvent event : events) {
            reportDtos.add(
                    ru.kostyanoy.api.dto.ReportDto.builder()
                            .penaltyEventID(event.getId())
                            .penaltyEventTimeStamp(event.getTimeStamp())
                            .fineType(event.getFine().getType())
                            .fineCharge(event.getFine().getCharge())
                            .carMake(event.getCar().getMake())
                            .carModel(event.getCar().getModel())
                            .fullStateNumber(event.getCar().getStateNumber().getFullNumber(validator))
                            .firstName(event.getCar().getCarOwner().getFirstName())
                            .middleName(event.getCar().getCarOwner().getMiddleName())
                            .lastName(event.getCar().getCarOwner().getLastName())
                            .build());
        }
        return reportDtos;
    }
}
