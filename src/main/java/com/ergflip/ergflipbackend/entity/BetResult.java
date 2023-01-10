package com.ergflip.ergflipbackend.entity;

public class BetResult {

    private boolean result;
    private Long amount;
    private String coin;

    private String txId;
    public BetResult(boolean result, Long amount, String coin, String txId) {
        this.result = result;
        this.amount = amount;
        this.coin = coin;
        this.txId = txId;
    }

    public BetResult() {}

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}