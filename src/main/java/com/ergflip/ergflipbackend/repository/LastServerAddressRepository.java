package com.ergflip.ergflipbackend.repository;

import com.ergflip.ergflipbackend.entity.ServerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastServerAddressRepository extends JpaRepository<ServerAddress, String> {

}
