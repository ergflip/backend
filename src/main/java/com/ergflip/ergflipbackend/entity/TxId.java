package com.ergflip.ergflipbackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TxId")
public class TxId {
    @Id
    @Column(name = "tx_id")
    private String tx_id;

    public TxId(String tx_id) {
        this.tx_id = tx_id;
    }

    public TxId() {}

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
    }
}
