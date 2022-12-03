package com.ergflip.ergflipbackend.service;

import com.ergflip.ergflipbackend.entity.Bet;
import com.ergflip.ergflipbackend.entity.BetResult;
import com.ergflip.ergflipbackend.entity.ServerAddress;
import com.ergflip.ergflipbackend.entity.TxId;
import com.ergflip.ergflipbackend.repository.AddressesRepository;
import com.ergflip.ergflipbackend.repository.LastServerAddressRepository;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import com.ergflip.ergflipbackend.utils.TransactionMapper;
import org.ergoplatform.appkit.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ReceiveBetService {

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private TxIdRepository txIdRepository;

    @Autowired
    private LastServerAddressRepository lastServerAddressRepository;
    public static final String NODE_MAINNET = "http://213.239.193.208:9053/";
    public static final String NODE_TESTNET = "http://213.239.193.208:9052/";
    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};

    private ReentrantLock mutex = new ReentrantLock();

    public BetResult receiveBet(Bet bet) {
            return startSpinning(bet.getTxId(), bet.getBettorAddress());
        }

    public BetResult startSpinning(String txId, String winnerAddress) {
        if(Math.random() < 0.50) {
            return sendErgsToWinner(txId,winnerAddress);
        }
        else {
            return checkFlipLost(txId, winnerAddress);
        }
    }

    public BetResult sendErgsToWinner(String txId, String winnerAddress) {

        String[] phrases = {phrase, phrase2, phrase3, phrase4}; //ENV
        String password = "";

        ErgoClient ergoClient = RestApiErgoClient.create(getDefaultNodeUrl(true), NetworkType.MAINNET, "", RestApiErgoClient.getDefaultExplorerUrl(NetworkType.MAINNET));

        //address receiving the tx.
        Address recipient = Address.create(winnerAddress);
        //amount to send to the winner
        Long amountToSend = TransactionMapper.getAmountToSend(txId);
        if (amountToSend > 5000000000L || amountToSend == 0L) {
            addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,"Timeout_AmountBigger" , amountToSend, true));
            return null;
        }
        ergoClient.execute((BlockchainContext ctx) -> {
            ErgoContract contract = recipient.toErgoContract();
            //start mutex
            try {
                mutex.lock();

                List<ServerAddress> latestServerAddresses = lastServerAddressRepository.findAll();
                int latestAddressUsed = latestServerAddresses.get(0).getAddress();
                lastServerAddressRepository.deleteAll();
                if (latestAddressUsed == 4) {
                    lastServerAddressRepository.save(new ServerAddress(1));
                }
                else lastServerAddressRepository.save(new ServerAddress(latestAddressUsed + 1));

                //address sending the tx.
                Address ownerAddress = Address.create(serverAddresses[latestAddressUsed-1]);

                UnsignedTransaction unsignedTransaction = BoxOperations.createForSender(ownerAddress, ctx)
                        .withAmountToSpend((long) (amountToSend * 1.95))
                        .withInputBoxesLoader(new ExplorerAndPoolUnspentBoxesLoader().withAllowChainedTx(true))
                        .withFeeAmount(2200000L)
                        .putToContractTxUnsigned(contract);

                Mnemonic mn = Mnemonic.create(phrases[latestAddressUsed-1].toCharArray(), password.toCharArray());

                SignedTransaction signedTx = ctx.newProverBuilder().withMnemonic(mn).withEip3Secret(0).build().sign(unsignedTransaction);
                addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,signedTx.getId() , amountToSend , true));
                return ctx.sendTransaction(signedTx);
            } finally{
                mutex.unlock();
            }
        });
        return new BetResult(true,(long) (amountToSend * 2), "ERG");
    }

    public int getLastAddress() {
        List<ServerAddress> latestServerAddresses = lastServerAddressRepository.findAll();
        return latestServerAddresses.get(0).getAddress();
    }

    public BetResult checkFlipLost(String txId, String winnerAddress) {
        Long amountToSend = TransactionMapper.getAmountToSend(txId);
        addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress, txId, amountToSend, false));
        return null;
    }
    private static String getDefaultNodeUrl(boolean mainNet) {
        return mainNet ? NODE_MAINNET : NODE_TESTNET;
    }
}
