package com.ergflip.ergflipbackend.controller;

import com.ergflip.ergflipbackend.entity.Address;
import com.ergflip.ergflipbackend.service.BiggestWinnersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"https://www.grandgambit.io"})
@RestController
public class BiggestWinnersController {

    @Autowired
    private BiggestWinnersService biggestWinnersService;

    @GetMapping("/biggestWinners")
    public List<Address> getBiggestWinners() {
        return biggestWinnersService.getBiggestWinners();
    }

    @GetMapping("/biggestSingleWinners")
    public List<Address> getBiggestSingleWinners() {
        return biggestWinnersService.getBiggestSingleWinners();
    }
}
