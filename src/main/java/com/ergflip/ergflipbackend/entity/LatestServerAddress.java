package com.ergflip.ergflipbackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "latest_address")
public class LatestServerAddress {

    @Column(name = "address")
    private int address;

    @Id
    @Column(name = "id")
    private String id;

    public LatestServerAddress(int address, String id) {
        this.address = address;
        this.id = id;
    }

    public LatestServerAddress() {}

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}