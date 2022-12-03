package com.ergflip.ergflipbackend.service;

import com.ergflip.ergflipbackend.entity.Address;
import com.ergflip.ergflipbackend.repository.BiggestWinnersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BiggestWinnersService {

    @Autowired
    private BiggestWinnersRepository biggestWinnersRepository;

    public List<Address> getBiggestWinners() {
        return biggestWinnersRepository.getBiggestWinners();
    }

    public List<Address> getBiggestSingleWinners() {
        return biggestWinnersRepository.getBiggestSingleWinners();
    }

}
