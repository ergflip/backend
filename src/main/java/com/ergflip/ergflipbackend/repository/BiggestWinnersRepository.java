package com.ergflip.ergflipbackend.repository;

import com.ergflip.ergflipbackend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BiggestWinnersRepository extends JpaRepository<Address, String> {

        @Query(value = "SELECT address, sum(bet_amount) as bet_amount, address as tx_id,true as won  FROM ergflip.addresses WHERE won = 1 GROUP BY address ORDER BY bet_amount DESC LIMIT 10;", nativeQuery = true)
        List<Address> getBiggestWinners();

        @Query(value= "SELECT address,bet_amount,tx_id, true as won FROM ergflip.addresses WHERE won = 1 ORDER BY bet_amount DESC LIMIT 10;", nativeQuery = true)
        List<Address> getBiggestSingleWinners();
}
