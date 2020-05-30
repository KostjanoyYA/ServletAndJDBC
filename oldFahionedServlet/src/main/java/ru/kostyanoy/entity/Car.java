package ru.kostyanoy.entity;

import java.util.Objects;

public class Car {

    private Long id;

    private String make;

    private String model;

    private StateNumber stateNumber;

    private CarOwner carOwner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public StateNumber getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(StateNumber stateNumber) {
        this.stateNumber = stateNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CarOwner getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(CarOwner carOwner) {
        this.carOwner = carOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id) &&
                Objects.equals(make, car.make) &&
                Objects.equals(stateNumber, car.stateNumber) &&
                Objects.equals(model, car.model) &&
                Objects.equals(carOwner, car.carOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, make, model, stateNumber, carOwner);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", stateNumber='" + stateNumber + '\'' +
                ", carOwner=" + carOwner +
                '}';
    }
}
