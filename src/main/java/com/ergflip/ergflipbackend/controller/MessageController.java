package com.ergflip.ergflipbackend.controller;

import com.ergflip.ergflipbackend.entity.*;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import com.ergflip.ergflipbackend.service.ReceiveBetService;
import com.ergflip.ergflipbackend.service.ReceiveRouletteBetService;
import com.ergflip.ergflipbackend.service.ReceiveTokenBetService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"https://www.grandgambit.io"})
@RestController
public class MessageController {

    @Autowired
    SimpMessagingTemplate template;
    //a
    @Autowired
    private TxIdRepository txIdRepository;
    @Autowired
    private ReceiveRouletteBetService receiveRouletteBetService;

    @Autowired
    private ReceiveBetService receiveBetService;

    @Autowired
    private ReceiveTokenBetService receiveTokenBetService;

    @PostMapping("/receiveRouletteBet")
    public RouletteBetResult receiveRouletteBet(@Validated @RequestBody RouletteBet bet) throws IllegalAccessException, ParseException, InterruptedException {
        System.out.println("Received bet: " + bet);
        System.out.println(bet.getTxId());
        if (txIdRepository.findById(bet.getTxId()).isEmpty()) {
            txIdRepository.save(new TxId(bet.getTxId()));
            System.out.println("Received bet1: " + bet);
            RouletteBetResult betResult = receiveRouletteBetService.receiveBet(bet);
            sendRouletteMessage(betResult);
        }
        return null;
    }

    @PostMapping("/sendRoulette")
    public ResponseEntity<Void> sendRouletteMessage(RouletteBetResult betResult) {
        System.out.println("Sending message to topic/message where front is subscribed");
        System.out.println(betResult.getTxId());
        template.convertAndSendToUser(betResult.getTxId(),"/private", betResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/receiveBet")
    public BetResult receiveBet(@Validated @RequestBody Bet bet) {
        if (txIdRepository.findById(bet.getTxId()).isEmpty()) {
            txIdRepository.save(new TxId(bet.getTxId()));
            BetResult betResult = receiveBetService.receiveBet(bet);
            sendBet(betResult);
        }
        return null;
    }

    @PostMapping("/sendBet")
    public ResponseEntity<Void> sendBet(BetResult betResult) {
        System.out.println("Sending message to topic/message where front is subscribed");
        System.out.println(betResult.getTxId());//
        template.convertAndSendToUser(betResult.getTxId(),"/private", betResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/receiveTokensBet")
    public BetResult receiveTokensBet(@Validated @RequestBody TokenBet bet) {
        if (txIdRepository.findById(bet.getTxId()).isEmpty()) {
            txIdRepository.save(new TxId(bet.getTxId()));
            BetResult betResult =  receiveTokenBetService.receiveBet(bet);
            sendBet(betResult);
        }
        return null;
    }

    @PostMapping("/receiveErgopayBet/{address}/{txId}/{multiplier}/{game}")
    public BetResult receiveBet(@PathVariable String address, @PathVariable String txId, @PathVariable String multiplier, @PathVariable String game) {
        sendErgopayTxSigned(address, txId, game);
        if (txIdRepository.findById(txId).isEmpty()) {
            txIdRepository.save(new TxId(txId));
            Bet bet = new Bet(address, txId, Integer.parseInt(multiplier));
            BetResult betResult = receiveBetService.receiveBet(bet);
            sendErgopayBet(betResult, address);
        }
        return null;
    }

    @PostMapping("/sendErgopayBet")
    public ResponseEntity<Void> sendErgopayBet(BetResult betResult, String address) {
        System.out.println(betResult.getTxId());
        template.convertAndSendToUser(betResult.getTxId(),"/private", betResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sendErgopayTxSigned")
    public ResponseEntity<Void> sendErgopayTxSigned(String address, String txId, String game) {
        System.out.println("Sending message to topic/message where front is subscribed VIA ERGOPAY");
        System.out.println("the txId is: " + txId);
        System.out.println("the address is: " + address);
        template.convertAndSendToUser(address,game, txId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/receiveErgopayBet/{address}/{txId}/{tokenId}/{multiplier}/{game}")
    public BetResult receiveBet(@PathVariable String address, @PathVariable String txId, @PathVariable String tokenId, @PathVariable String multiplier, @PathVariable String game) {
        sendErgopayTxSigned(address, txId, game);
        if (txIdRepository.findById(txId).isEmpty()) {
            txIdRepository.save(new TxId(txId));
            Bet bet = new Bet(address, txId, Integer.parseInt(multiplier));
            BetResult betResult = receiveTokenBetService.receiveBet(new TokenBet(address, txId, tokenId, Integer.parseInt(multiplier)));
            sendErgopayBet(betResult, address);
        }
        return null;
    }

    @PostMapping("/receiveRouletteErgopayBet/{address}/{txId}")
    public void receiveRouletteErgopayBet(@PathVariable String address, @PathVariable String txId) throws IllegalAccessException, ParseException, InterruptedException {
        System.out.println("llego aqui1");
        sendErgopayTxSigned(address, txId, "roulette");
    }


}