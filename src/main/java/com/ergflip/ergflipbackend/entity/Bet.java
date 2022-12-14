package com.ergflip.ergflipbackend.entity;

import java.io.Serializable;

public class Bet implements Serializable {
    private String bettorAddress;
    private String txId;
    private int multiplier;

    public Bet(String bettorAddress, String txId, int multiplier) {
        this.bettorAddress = bettorAddress;
        this.txId = txId;
        this.multiplier = multiplier;
    }

    public Bet() {}

    public String getBettorAddress() {
        return bettorAddress;
    }

    public void setBettorAddress(String bettorAddress) {
        this.bettorAddress = bettorAddress;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }


    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}