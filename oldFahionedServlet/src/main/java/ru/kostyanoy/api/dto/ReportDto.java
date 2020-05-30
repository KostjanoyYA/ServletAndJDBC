package ru.kostyanoy.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonDeserialize(builder = ReportDto.Builder.class)
public class ReportDto {

    private final Long penaltyEventID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime penaltyEventTimeStamp;

    private final String fineType;

    private final BigDecimal fineCharge;

    private String carMake;

    private String carModel;

    private String fullStateNumber;

    private String firstName;

    private String middleName;

    private String lastName;


    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long penaltyEventID;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime penaltyEventTimeStamp;

        private String fineType;

        private BigDecimal fineCharge;

        private String carMake;

        private String carModel;

        private String fullStateNumber;

        private String firstName;

        private String middleName;

        private String lastName;

        private Builder() {
        }

        private Builder(ReportDto eventDto) {
            this.penaltyEventID = eventDto.penaltyEventID;
            this.penaltyEventTimeStamp = eventDto.penaltyEventTimeStamp;
            this.fineType = eventDto.fineType;
            this.fineCharge = eventDto.fineCharge;
            this.carMake = eventDto.carMake;
            this.carModel = eventDto.carModel;
            this.fullStateNumber = eventDto.fullStateNumber;
            this.firstName = eventDto.firstName;
            this.middleName = eventDto.middleName;
            this.lastName = eventDto.lastName;
        }

        public Builder penaltyEventID(Long id) {
            this.penaltyEventID = id;
            return this;
        }

        public Builder penaltyEventTimeStamp(LocalDateTime timeStamp) {
            this.penaltyEventTimeStamp = timeStamp;
            return this;
        }

        public Builder fineType(String type) {
            this.fineType = type;
            return this;
        }

        public Builder fineCharge(BigDecimal charge) {
            this.fineCharge = charge;
            return this;
        }

        public Builder carMake(String carMake) {
            this.carMake = carMake;
            return this;
        }

        public Builder carModel(String carModel) {
            this.carModel = carModel;
            return this;
        }

        public Builder fullStateNumber(String fullStateNumber) {
            this.fullStateNumber = fullStateNumber;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ReportDto build() {
            return new ReportDto(this.penaltyEventID, this.penaltyEventTimeStamp, this.fineType, this.fineCharge,
                    this.carMake, this.carModel, this.fullStateNumber, this.firstName, this.middleName, this.lastName);
        }
    }

    private ReportDto(Long penaltyEventID, LocalDateTime penaltyEventTimeStamp, String fineType, BigDecimal fineCharge,
                      String carMake, String carModel, String fullStateNumber, String firstName, String middleName,
                      String lastName) {
        this.penaltyEventID = penaltyEventID;
        this.penaltyEventTimeStamp = penaltyEventTimeStamp;
        this.fineType = fineType;
        this.fineCharge = fineCharge;
        this.carMake = carMake;
        this.carModel = carModel;
        this.fullStateNumber = fullStateNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Long getPenaltyEventID() {
        return penaltyEventID;
    }

    public LocalDateTime getPenaltyEventTimeStamp() { return penaltyEventTimeStamp; }

    public String getFineType() { return fineType;}

    public BigDecimal getFineCharge() { return fineCharge;}

    public String getCarMake() { return carMake;}

    public String getCarModel() { return carModel;}

    public String getFullStateNumber() { return fullStateNumber;}

    public String getFirstName() { return firstName;}

    public String getMiddleName() { return middleName;}

    public String getLastName() { return lastName;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDto eventDto = (ReportDto) o;
        return Objects.equals(penaltyEventID, eventDto.penaltyEventID) &&
                Objects.equals(penaltyEventTimeStamp, eventDto.penaltyEventTimeStamp) &&
                Objects.equals(fineType, eventDto.fineType) &&
                Objects.equals(fineCharge, eventDto.fineCharge) &&
                Objects.equals(carMake, eventDto.carMake) &&
                Objects.equals(carModel, eventDto.carModel) &&
                Objects.equals(fullStateNumber, eventDto.fullStateNumber) &&
                Objects.equals(firstName, eventDto.firstName) &&
                Objects.equals(middleName, eventDto.middleName) &&
                Objects.equals(lastName, eventDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(penaltyEventID, penaltyEventTimeStamp, fineType, fineCharge,
                carMake, carModel, fullStateNumber, firstName, middleName, lastName);
    }

    @Override
    public String toString() {
        return "ReportDto{" +
                "penaltyEventID=" + penaltyEventID +
                ", penaltyEventTimeStamp=" + penaltyEventTimeStamp +
                ", fineType='" + fineType + '\'' +
                ", fineCharge=" + fineCharge +
                ", carMake='" + carMake + '\'' +
                ", carModel='" + carModel + '\'' +
                ", fullStateNumber='" + fullStateNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
