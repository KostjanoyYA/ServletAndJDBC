package ru.kostyanoy.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonDeserialize(builder = PenaltyEventDto.Builder.class)
public class PenaltyEventDto {

    private final Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timeStamp;

    private final Long carID;

    private final Long fineID;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timeStamp;

        private Long carID;

        private Long fineID;

        private Builder() {
        }

        private Builder(PenaltyEventDto eventDto) {
            this.id = eventDto.id;
            this.timeStamp = eventDto.timeStamp;
            this.carID = eventDto.carID;
            this.fineID = eventDto.fineID;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder timeStamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder carID(Long carID) {
            this.carID = carID;
            return this;
        }

        public Builder fineID(Long fineID) {
            this.fineID = fineID;
            return this;
        }

        public PenaltyEventDto build() {
            return new PenaltyEventDto(this.id, this.timeStamp, this.carID, this.fineID);
        }
    }

    private PenaltyEventDto(Long id, LocalDateTime timeStamp, Long carID, Long fineID) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.carID = carID;
        this.fineID = fineID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Long getCarID() {
        return carID;
    }

    public Long getFineID() {
        return fineID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PenaltyEventDto eventDto = (PenaltyEventDto) o;
        return Objects.equals(id, eventDto.id) &&
                Objects.equals(timeStamp, eventDto.timeStamp) &&
                Objects.equals(carID, eventDto.carID) &&
                Objects.equals(fineID, eventDto.fineID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeStamp, carID, fineID);
    }

    @Override
    public String toString() {
        return "PenaltyEventDto{" +
                "id=" + id +
                ", timeStamp='" + timeStamp + '\'' +
                ", carID='" + carID + '\'' +
                ", fineID='" + fineID + '\'' +
                '}';
    }
}
