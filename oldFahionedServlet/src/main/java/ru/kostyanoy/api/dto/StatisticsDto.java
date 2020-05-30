package ru.kostyanoy.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = StatisticsDto.Builder.class)
public class StatisticsDto {

    private Long fineID;

    private String fineType;

    private Long topPlace;

    private Long occurrencesNumber;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long fineID;

        private String fineType;

        private Long topPlace;

        private Long occurrencesNumber;

        private Builder() {
        }

        private Builder(StatisticsDto statisticsDto) {
            this.fineID = statisticsDto.fineID;
            this.fineType = statisticsDto.fineType;
            this.topPlace = statisticsDto.topPlace;
            this.occurrencesNumber = statisticsDto.occurrencesNumber;
        }


        public Builder fineID(Long fineID) {
            this.fineID = fineID;
            return this;
        }

        public Builder fineType(String fineType) {
            this.fineType = fineType;
            return this;
        }

        public Builder topPlace(Long topPlace) {
            this.topPlace = topPlace;
            return this;
        }

        public Builder occurrencesNumber(Long occurrencesNumber) {
            this.occurrencesNumber = occurrencesNumber;
            return this;
        }

        public StatisticsDto build() {
            return new StatisticsDto(this.fineID, this.fineType, this.topPlace, this.occurrencesNumber);
        }
    }

    private StatisticsDto(Long fineID, String fineType, Long topPlace, Long occurrencesNumber) {
        this.fineID = fineID;
        this.fineType = fineType;
        this.topPlace = topPlace;
        this.occurrencesNumber = occurrencesNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Long getFineID() { return fineID; }

    public String getFineType() { return fineType; }

    public Long getTopPlace() {
        return topPlace;
    }

    public Long getOccurrencesNumber() {
        return occurrencesNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsDto statisticsDto = (StatisticsDto) o;
        return Objects.equals(fineID, statisticsDto.fineID) &&
                Objects.equals(fineType, statisticsDto.fineType) &&
                Objects.equals(topPlace, statisticsDto.topPlace) &&
                Objects.equals(occurrencesNumber, statisticsDto.occurrencesNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fineID, topPlace, occurrencesNumber);
    }

    @Override
    public String toString() {
        return "StatisticsDto{" +
                ", fineID='" + fineID.toString() + '\'' +
                ", fineType='" + fineType + '\'' +
                ", topPlace='" + topPlace + '\'' +
                ", occurrencesNumber='" + occurrencesNumber + '\'' +
                '}';
    }
}
