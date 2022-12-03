package com.ergflip.ergflipbackend.service;

import com.ergflip.ergflipbackend.entity.*;
import com.ergflip.ergflipbackend.repository.AddressesRepository;
import com.ergflip.ergflipbackend.repository.LastServerAddressRepository;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import com.ergflip.ergflipbackend.utils.TransactionMapper;
import org.ergoplatform.appkit.*;
import org.ergoplatform.appkit.Address;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import static com.ergflip.ergflipbackend.utils.TokenUtils.fromTokenIdToName;
import static com.ergflip.ergflipbackend.utils.TokenUtils.maximumAmountToBetPerToken;

@Service
public class ReceiveTokenBetService {

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private TxIdRepository txIdRepository;

    @Autowired
    private LastServerAddressRepository lastServerAddressRepository;
    public static final String NODE_MAINNET = "http://213.239.193.208:9053/";
    public static final String NODE_TESTNET = "http://213.239.193.208:9052/";
    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};
    private static final String BALANCE_API = "https://api.ergoplatform.com/api/v1/addresses/9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte/balance/total";

    private ReentrantLock mutex = new ReentrantLock();

    public BetResult receiveBet(TokenBet bet) {
            return startSpinning(bet.getTxId(), bet.getBettorAddress(), bet.getTokenId());
        }

    public BetResult startSpinning(String txId, String winnerAddress, String tokenId) {
        if(Math.random() < 0.50) {
            return sendErgsToWinner(txId,winnerAddress,tokenId);
        }
        else {
            checkFlipLost(txId, winnerAddress,tokenId);
            return null;
        }
    }

    public BetResult sendErgsToWinner(String txId, String winnerAddress,String tokenId) {

        String[] phrases = {phrase, phrase2, phrase3, phrase4};
        String password = "";

        ErgoClient ergoClient = RestApiErgoClient.create(getDefaultNodeUrl(true), NetworkType.MAINNET, "", RestApiErgoClient.getDefaultExplorerUrl(NetworkType.MAINNET));

        //address receiving the tx.
        Address recipient = Address.create(winnerAddress);
        //amount to send to the winner
        List<Long> amountAndDencimals = TransactionMapper.getTokenAmountToSend(txId, tokenId);
        Long amountToSend = amountAndDencimals.get(0);
        Long decimals = amountAndDencimals.get(1);
        if (amountToSend > maximumAmountToBetPerToken(tokenId) || amountToSend == 0L) {
            addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,"Timeout_AmountBigger" , (long)(amountToSend*1.95), true));
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

                List<ErgoToken> ergoTokens = Arrays.asList(new ErgoToken(tokenId, (long)(amountToSend*1.95)));
                UnsignedTransaction unsignedTransaction = BoxOperations.createForSender(ownerAddress, ctx)
                        .withAmountToSpend(Parameters.MinChangeValue)
                        .withTokensToSpend(ergoTokens)
                        .withInputBoxesLoader(new ExplorerAndPoolUnspentBoxesLoader().withAllowChainedTx(true))
                        .withFeeAmount(2200000L)
                        .putToContractTxUnsigned(contract);
                Mnemonic mn = Mnemonic.create(phrases[latestAddressUsed-1].toCharArray(), password.toCharArray());

                SignedTransaction signedTx = ctx.newProverBuilder().withMnemonic(mn).withEip3Secret(0).build().sign(unsignedTransaction);
                addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,signedTx.getId() , (long)(amountToSend*1.95) , true));
                return ctx.sendTransaction(signedTx);
            } finally {
            mutex.unlock();
            }
            });
        return new BetResult(true,(long)(amountToSend*2),fromTokenIdToName(tokenId));
    }

    public BetResult checkFlipLost(String txId, String winnerAddress, String tokenId) {
        List<Long> amountAndDecimals = TransactionMapper.getTokenAmountToSend(txId, tokenId);
        Long amountToSend = amountAndDecimals.get(0);
        Long decimals = amountAndDecimals.get(1);
        addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress, txId, (long)(amountToSend*1.95), false));
        return null;
    }

    private static String getDefaultNodeUrl(boolean mainNet) {
        return mainNet ? NODE_MAINNET : NODE_TESTNET;
    }
}
