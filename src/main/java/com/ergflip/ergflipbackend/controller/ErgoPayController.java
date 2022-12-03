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

@RestController
public class ErgoPayController {

    @Autowired
    private ErgoPayService ergoPayService;

    @Autowired
    private ReceiveBetService receiveBetService;

    @Autowired
    private ReceiveTokenBetService receiveTokenBetService;

    @Autowired
    private TxIdRepository txIdRepository;

    @CrossOrigin(origins = {"https://www.ergflip.com/"})
    @GetMapping("/roundTrip/{address}/{amount}")
    public ErgoPayResponse roundTrip(@PathVariable String address, @PathVariable Long amount) {
        return ergoPayService.roundTrip(address, amount);
    }

    @CrossOrigin(origins = {"https://www.ergflip.com/"})
    @GetMapping("/roundTrip/{address}/{amount}/{tokenId}")
    public ErgoPayResponse roundTokenTrip(@PathVariable String address, @PathVariable Long amount, @PathVariable String tokenId) {
        return ergoPayService.roundTokenTrip(address, amount, tokenId);
    }

    @CrossOrigin(origins = {"https://www.ergflip.com/"})
    @PostMapping("/receiveErgopayBet/{address}/{txId}")
    public BetResult receiveBet(@PathVariable String address, @PathVariable String txId) {
        if (txIdRepository.findById(txId).isEmpty()) {
            txIdRepository.save(new TxId(txId));
            return receiveBetService.receiveBet(new Bet(address, txId));
        }
        else return null;
    }

    @CrossOrigin(origins = {"https://www.ergflip.com/"})
    @PostMapping("/receiveErgopayBet/{address}/{txId}/{tokenId}")
    public BetResult receiveBet(@PathVariable String address, @PathVariable String txId, @PathVariable String tokenId) {
        if (txIdRepository.findById(txId).isEmpty()) {
            txIdRepository.save(new TxId(txId));
            return receiveTokenBetService.receiveBet(new TokenBet(address, txId, tokenId));
        }
        else return null;
    }


}
