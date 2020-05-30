package ru.kostyanoy.entity;

import java.util.Optional;

public class StateNumber {

    private Long id;

    private String country;

    private int regionCode;

    private String series;

    private int number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFullNumber(StateNumberValidator validator) {
        return validator.generateFullNumber(this);
    }

    public boolean setStateNumber(String fullNumber, StateNumberValidator validator) {
        Optional<StateNumber> parsedStateNumber = validator.parseStateNumber(fullNumber);

        if (!parsedStateNumber.isPresent()) {
            return false;
        }
        series = parsedStateNumber.get().getSeries();
        number = parsedStateNumber.get().getNumber();
        regionCode = parsedStateNumber.get().getRegionCode();
        country = parsedStateNumber.get().getCountry();

        return true;
    }


    @Override
    public String toString() {
        return "StateNumber{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", regionCode=" + regionCode +
                ", series='" + series + '\'' +
                ", number=" + number +
                '}';
    }
}
