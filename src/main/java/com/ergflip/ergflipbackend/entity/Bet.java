package com.ergflip.ergflipbackend.entity;

import java.io.Serializable;

public class Bet implements Serializable {
    private String bettorAddress;
    private String txId;

    public Bet(String bettorAddress, String txId) {
        this.bettorAddress = bettorAddress;
        this.txId = txId;
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
}
