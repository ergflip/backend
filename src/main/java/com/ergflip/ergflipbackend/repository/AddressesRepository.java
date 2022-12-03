package com.ergflip.ergflipbackend.repository;

import com.ergflip.ergflipbackend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressesRepository extends JpaRepository<Address, String> {

}
