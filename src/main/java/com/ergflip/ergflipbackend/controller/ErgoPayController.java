package com.ergflip.ergflipbackend.controller;

import com.ergflip.ergflipbackend.entity.*;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import com.ergflip.ergflipbackend.service.ErgoPayService;
import com.ergflip.ergflipbackend.service.ReceiveBetService;
import com.ergflip.ergflipbackend.service.ReceiveTokenBetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"https://www.grandgambit.io"})
@RestController
public class ErgoPayController {

    @Autowired
    private ErgoPayService ergoPayService;

    @GetMapping("/roundTrip/{address}/{amount}/{multiplier}/{game}")
    public ErgoPayResponse roundTrip(@PathVariable String address, @PathVariable Long amount, @PathVariable String multiplier, @PathVariable String game) {
        return ergoPayService.roundTrip(address, amount, multiplier, game);
    }

    @GetMapping("/roundTrip/{address}/{amount}/{tokenId}/{multiplier}/{game}")
    public ErgoPayResponse roundTokenTrip(@PathVariable String address, @PathVariable Long amount, @PathVariable String tokenId, @PathVariable String multiplier, @PathVariable String game) {
        return ergoPayService.roundTokenTrip(address, amount, tokenId, multiplier, game);
    }

    @GetMapping("/roundTripRoulette/{address}/{amount}/{tokenId}")
    public ErgoPayResponse rouletteRoundTrip(@PathVariable String address, @PathVariable Long amount, @PathVariable String tokenId) {
        return ergoPayService.roundRouletteTrip(address, amount, tokenId);
    }


}
