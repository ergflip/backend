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

@CrossOrigin(origins = {"https://www.grandgambit.io"})
@RestController
public class ReceiveBetController {

    @Autowired
    private ReceiveBetService receiveBetService;

    @GetMapping("/backendReachable")
    public int backendReachable() {
        if (serverWalletHasEnoughFunds()) {
            return receiveBetService.getLastAddress();
        }
        else return 0;
    }
}
