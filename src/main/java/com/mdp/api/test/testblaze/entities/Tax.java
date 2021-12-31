package com.mdp.api.test.testblaze.entities;

public enum Tax {

    CITY_TAX(0.1),
    COUNTY_TAX(0.05),
    STATE_TAX(0.08),
    FEDERAL_TAX(0.02);


    private final Double value;

    Tax(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
