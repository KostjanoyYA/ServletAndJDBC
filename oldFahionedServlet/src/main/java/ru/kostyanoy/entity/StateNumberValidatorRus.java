package ru.kostyanoy.entity;

import java.util.Optional;

public class StateNumberValidatorRus implements StateNumberValidator {
    private final static int maxLength = 12;
    private final static int minLength = 12;
    private static final String literals = "АВЕКМНОРСТУХ" + "ABEKMHOPCTYX";
    private static final int lowerNumberLimit = 1;
    private static final int upperNumberLimit = 999;
    private static final int lowerRegionCodeLimit = 1;
    private static final int upperRegionCodeLimit = 999;
    private static final String country = "RUS";

    @Override
    public Optional<StateNumber> parseStateNumber(String fullNumber) {

        if (fullNumber == null || fullNumber.isEmpty()) {
            return Optional.empty();
        }

        if (fullNumber.length() > maxLength || fullNumber.length() < minLength) {
            return Optional.empty();
        }

        StateNumber stateNumber = new StateNumber();
        String fullNumberUpperCase = fullNumber.toUpperCase().trim();

        if (fullNumberUpperCase.indexOf(country, fullNumberUpperCase.length() - country.length() - 1) < 0) {
            return Optional.empty();
        }
        stateNumber.setCountry(country);

        if (!(literals.contains(String.valueOf(fullNumberUpperCase.charAt(0)))
                && literals.contains(String.valueOf(fullNumberUpperCase.charAt(4)))
                && literals.contains(String.valueOf(fullNumberUpperCase.charAt(5))))) {
            return Optional.empty();
        }
        stateNumber.setSeries(String.valueOf(
                new char[]{fullNumberUpperCase.charAt(0), fullNumberUpperCase.charAt(4), fullNumberUpperCase.charAt(5)}));
        int number;
        if ((number = parseNumericPart(fullNumberUpperCase.substring(1, 4), upperNumberLimit, lowerNumberLimit)) < 0) {
            return Optional.empty();
        }
        stateNumber.setNumber(number);

        int regionCode;
        if ((regionCode = parseNumericPart(fullNumberUpperCase.substring(6, 9), upperRegionCodeLimit, lowerRegionCodeLimit)) < 0) {
            return Optional.empty();
        }
        stateNumber.setRegionCode(regionCode);

        return Optional.of(stateNumber);
    }

    private int parseNumericPart(String stringNumber, int upperLimit, int lowerLimit) {
        int result;
        try {
            result = Integer.parseInt(stringNumber);
        } catch (NumberFormatException e) {
            return -1;
        }
        return ((result >= lowerLimit) && (result <= upperLimit)) ? result : -1;
    }

    @Override
    public String generateFullNumber(StateNumber stateNumber) {
        return stateNumber.getSeries().charAt(0)
                + String.format("%03d", stateNumber.getNumber())
                + stateNumber.getSeries().substring(1)
                + String.format("%03d", stateNumber.getRegionCode())
                + stateNumber.getCountry();
    }
}
