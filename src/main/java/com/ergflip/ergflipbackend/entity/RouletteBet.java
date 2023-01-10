package com.ergflip.ergflipbackend.entity;

import java.io.Serializable;

public class RouletteBet implements Serializable {
    private String bettorAddress;
    private String txId;

    private String tokenId;

    private tableBet tableBet;

    public RouletteBet(String bettorAddress, String txId, String tokenId, tableBet tableBet) {
        this.bettorAddress = bettorAddress;
        this.txId = txId;
        this.tokenId = tokenId;
        this.tableBet = tableBet;
    }

    public RouletteBet() {}

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

    public tableBet getTableBet() {
        return tableBet;
    }

    public void setTableBet(tableBet tableBet) {
        this.tableBet = tableBet;
    }
}