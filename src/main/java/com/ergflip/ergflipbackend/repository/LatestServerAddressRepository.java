package com.ergflip.ergflipbackend.repository;
import com.ergflip.ergflipbackend.entity.LatestServerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LatestServerAddressRepository extends JpaRepository<LatestServerAddress, String> {

}