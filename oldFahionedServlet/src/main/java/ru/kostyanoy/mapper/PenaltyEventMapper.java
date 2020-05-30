package ru.kostyanoy.mapper;

import ru.kostyanoy.entity.PenaltyEvent;

public class PenaltyEventMapper {

    private static final PenaltyEventMapper INSTANCE = new PenaltyEventMapper();

    private PenaltyEventMapper() {
    }

    public static PenaltyEventMapper getInstance() {
        return INSTANCE;
    }

    public ru.kostyanoy.api.dto.PenaltyEventDto toDto(PenaltyEvent penaltyEvent) {
        return ru.kostyanoy.api.dto.PenaltyEventDto.builder()
                .id(penaltyEvent.getId())
                .timeStamp(penaltyEvent.getTimeStamp())
                .carID(penaltyEvent.getCar().getStateNumber().getId())
                .fineID(penaltyEvent.getFine().getId())
                .build();
    }
}
