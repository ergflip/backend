package com.ergflip.ergflipbackend.repository;

import com.ergflip.ergflipbackend.entity.TxId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxIdRepository extends JpaRepository<TxId, String> {

}
