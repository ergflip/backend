package com.ergflip.ergflipbackend.entity;

import java.io.Serializable;

public class TokenBet implements Serializable {
    private String bettorAddress;
    private String txId;

    private String tokenId;

    private int multiplier;

    public TokenBet(String bettorAddress, String txId, String tokenId, int multiplier) {
        this.bettorAddress = bettorAddress;
        this.txId = txId;
        this.tokenId = tokenId;
        this.multiplier = multiplier;
    }

    public TokenBet() {}

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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}