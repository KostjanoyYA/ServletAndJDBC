package ru.kostyanoy.entity;

import java.util.Objects;

public class Statistics {

    private Fine fine;

    private Long topPlace;

    private Long occurrencesNumber;

    public Fine getFine() {
        return fine;
    }

    public void setFine(Fine fine) {
        this.fine = fine;
    }

    public Long getTopPlace() {
        return topPlace;
    }

    public void setTopPlace(Long topPlace) {
        this.topPlace = topPlace;
    }

    public Long getOccurrencesNumber() {
        return occurrencesNumber;
    }

    public void setOccurrencesNumber(Long occurrencesNumber) {
        this.occurrencesNumber = occurrencesNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics statistics = (Statistics) o;
        return Objects.equals(fine, statistics.fine) &&
                Objects.equals(topPlace, statistics.topPlace) &&
                Objects.equals(occurrencesNumber, statistics.occurrencesNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fine, topPlace, occurrencesNumber);
    }

    @Override
    public String toString() {
        return "statistics{" +
                ", fine='" + fine + '\'' +
                ", topPlace='" + topPlace + '\'' +
                ", occurrencesNumber='" + occurrencesNumber + '\'' +
                '}';
    }
}
