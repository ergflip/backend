package com.ergflip.ergflipbackend.controller;

import com.ergflip.ergflipbackend.entity.Bet;
import com.ergflip.ergflipbackend.entity.BetResult;
import com.ergflip.ergflipbackend.entity.TokenBet;
import com.ergflip.ergflipbackend.entity.TxId;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import com.ergflip.ergflipbackend.service.ReceiveBetService;
import com.ergflip.ergflipbackend.service.ReceiveTokenBetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ergflip.ergflipbackend.utils.ApiUtils.serverWalletHasEnoughFunds;

@RestController
public class ReceiveBetController {

    @Autowired
    private ReceiveBetService receiveBetService;

    @Autowired
    private ReceiveTokenBetService receiveTokenBetService;

    @Autowired
    private TxIdRepository txIdRepository;
    /*@CrossOrigin(origins = "https://www.ergflip.com/")*/
    @CrossOrigin(origins = {"https://www.ergflip.com/"})
    @GetMapping("/backendReachable")
    public int backendReachable() {
        if (serverWalletHasEnoughFunds()) {
            return receiveBetService.getLastAddress();
        }
        else return 0;
    }

    /*@CrossOrigin(origins = "https://www.ergflip.com/")*/
    @CrossOrigin(origins = {"https://www.ergflip.com/"})
    @PostMapping("/receiveBet")
    public BetResult receiveBet(@Validated @RequestBody Bet bet) {
        if (txIdRepository.findById(bet.getTxId()).isEmpty()) {
            txIdRepository.save(new TxId(bet.getTxId()));
            return receiveBetService.receiveBet(bet);
        }
        else return null;
    }

    @CrossOrigin(origins = {"https://www.ergflip.com/"})
    @PostMapping("/receiveTokensBet")
    public BetResult receiveTokensBet(@Validated @RequestBody TokenBet bet) {
        if (txIdRepository.findById(bet.getTxId()).isEmpty()) {
            txIdRepository.save(new TxId(bet.getTxId()));
            return receiveTokenBetService.receiveBet(bet);
        }
        else return null;
    }
}
