package com.ergflip.ergflipbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Column(name = "address")
    private String address;

    @Id
    @Column(name = "txId")
    private String txId;

    @Column(name = "bet_amount")
    private Long bet_amount;

    @Column(name = "won")
    private boolean won;

    public Address(String address, String txId, Long bet_amount, boolean won) {
        this.address = address;
        this.txId = txId;
        this.bet_amount = bet_amount;
        this.won = won;
    }

    public Address(String address, String txId, Long bet_amount) {
        this.address = address;
        this.txId = txId;
        this.bet_amount = bet_amount;
    }

    public Address(String address, Long bet_amount) {
        this.address = address;
        this.bet_amount = bet_amount;
    }

    public Address() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBetAmount() {
        return bet_amount;
    }

    public void setBetAmount(Long betAmount) {
        this.bet_amount = betAmount;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
