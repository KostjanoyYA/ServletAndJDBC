package ru.kostyanoy.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class PenaltyEvent {

    private Long id;

    private LocalDateTime timeStamp;

    private Fine fine;

    private Car car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Fine getFine() {
        return fine;
    }

    public void setFine(Fine fine) {
        this.fine = fine;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PenaltyEvent event = (PenaltyEvent) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(timeStamp, event.timeStamp) &&
                Objects.equals(fine, event.fine) &&
                Objects.equals(car, event.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeStamp, fine, car);
    }

    @Override
    public String toString() {
        return "PenaltyEvent{" +
                "id=" + id +
                ", timeStamp='" + timeStamp + '\'' +
                ", fine='" + fine + '\'' +
                ", car='" + car + '\'' +
                '}';
    }
}
