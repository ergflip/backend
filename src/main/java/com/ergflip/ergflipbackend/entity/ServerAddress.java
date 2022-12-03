package com.ergflip.ergflipbackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "server_address")
public class ServerAddress {
    @Id
    @Column(name = "address")
    private int address;

    public ServerAddress(int address) {
        this.address = address;
    }

    public ServerAddress() {}

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
