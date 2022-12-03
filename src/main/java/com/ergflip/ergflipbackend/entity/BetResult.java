package com.ergflip.ergflipbackend.entity;

public class BetResult {

    private boolean result;
    private Long amount;
    private String coin;

    public BetResult(boolean result, Long amount, String coin) {
        this.result = result;
        this.amount = amount;
        this.coin = coin;
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
}
