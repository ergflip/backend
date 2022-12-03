package com.ergflip.ergflipbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Register {
    private String serializedValue;
    private String sigmaType;
    private String renderedValue;

    public Register(String serializedValue, String sigmaType, String renderedValue) {
        this.serializedValue = serializedValue;
        this.sigmaType = sigmaType;
        this.renderedValue = renderedValue;
    }

    public String getSerializedValue() {
        return serializedValue;
    }

    public void setSerializedValue(String serializedValue) {
        this.serializedValue = serializedValue;
    }

    public String getSigmaType() {
        return sigmaType;
    }

    public void setSigmaType(String sigmaType) {
        this.sigmaType = sigmaType;
    }

    public String getRenderedValue() {
        return renderedValue;
    }

    public void setRenderedValue(String renderedValue) {
        this.renderedValue = renderedValue;
    }

    @Override
    public String toString() {
        return "Register{" +
                "serializedValue='" + serializedValue + '\'' +
                ", sigmaType='" + sigmaType + '\'' +
                ", renderedValue='" + renderedValue + '\'' +
                '}';
    }
}
