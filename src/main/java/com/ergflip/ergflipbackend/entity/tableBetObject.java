package com.ergflip.ergflipbackend.entity;

public class tableBetObject {
    private String [] values;
    private double multiplier;

    public tableBetObject() {}

    public tableBetObject(String[] values, double multiplier) {
        this.values = values;
        this.multiplier = multiplier;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}