package com.ergflip.ergflipbackend.service;

import com.ergflip.ergflipbackend.entity.*;
import com.ergflip.ergflipbackend.repository.AddressesRepository;
import com.ergflip.ergflipbackend.repository.LastServerAddressRepository;
import com.ergflip.ergflipbackend.repository.LatestServerAddressRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.SplittableRandom;
import java.util.concurrent.locks.ReentrantLock;

import static com.ergflip.ergflipbackend.utils.Utils.getMultiplier;

@Service
public class ReceiveBetService {

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private LatestServerAddressRepository latestServerAddressRepository;
    public static final String NODE_MAINNET = "http://213.239.193.208:9053/";
    public static final String NODE_TESTNET = "http://213.239.193.208:9052/";
    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};

    private ReentrantLock mutex = new ReentrantLock();

    public BetResult receiveBet(Bet bet) {
            return startSpinning(bet.getTxId(), bet.getBettorAddress(), bet.getMultiplier());
        }

    public BetResult startSpinning(String txId, String winnerAddress, int multiplier) {
        if (multiplier == 2) {
            if(Math.random() < 0.50) {
                return sendErgsToWinner(txId,winnerAddress, multiplier);
            }
            else {
                return checkFlipLost(txId, winnerAddress);
            }
        }
        else if (multiplier == 6){
            SplittableRandom random = new SplittableRandom();
            if(random.nextInt(60) < 10) {
                return sendErgsToWinner(txId,winnerAddress, multiplier);
            }
            else {
                return checkFlipLost(txId, winnerAddress);
            }
        } else {
            SplittableRandom random = new SplittableRandom();
            if(random.nextInt(100) < 10) {
                return sendErgsToWinner(txId,winnerAddress, multiplier);
            }
            else {
                return checkFlipLost(txId, winnerAddress);
            }
        }

    }
    public BetResult sendErgsToWinner(String txId, String winnerAddress, int multiplier) {

        Optional<LatestServerAddress> latestServerAddresses = latestServerAddressRepository.findById("1");
        int latestAddressUsed = latestServerAddresses.get().getAddress();
        if (latestAddressUsed == 4) {
            latestServerAddressRepository.save(new LatestServerAddress(1,"1"));
        }
        else latestServerAddressRepository.save(new LatestServerAddress(latestAddressUsed + 1,"1"));
        String[] phrases = {"phrase", "phrase2", "phrase3", "phrase4"}; //ENV
        String password = "";

        ErgoClient ergoClient = RestApiErgoClient.create(getDefaultNodeUrl(true), NetworkType.MAINNET, "", RestApiErgoClient.getDefaultExplorerUrl(NetworkType.MAINNET));

        //address receiving the tx.
        Address recipient = Address.create(winnerAddress);
        //amount to send to the winner
        Long amountReceived = TransactionMapper.getAmountToSend(txId);
        if (amountReceived > 11000000000L  || amountReceived == 0L) {
            addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,"Timeout_AmountBigger" , amountReceived, true));
            return null;
        }
        ergoClient.execute((BlockchainContext ctx) -> {
            ErgoContract contract = recipient.toErgoContract();
            //address sending the tx.
            Address ownerAddress = Address.create(serverAddresses[latestAddressUsed-1]);

            UnsignedTransaction unsignedTransaction = BoxOperations.createForSender(ownerAddress, ctx)
                    .withAmountToSpend((long) (amountReceived * getMultiplier(multiplier)))
                    .withInputBoxesLoader(new ExplorerAndPoolUnspentBoxesLoader().withAllowChainedTx(true))
                    .withFeeAmount(1100000L)
                    .putToContractTxUnsigned(contract);

            Mnemonic mn = Mnemonic.create(phrases[latestAddressUsed-1].toCharArray(), password.toCharArray());

            SignedTransaction signedTx = ctx.newProverBuilder().withMnemonic(mn).withEip3Secret(0).build().sign(unsignedTransaction);
            addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,signedTx.getId() , (long)(amountReceived * getMultiplier(multiplier)) , true));
            return ctx.sendTransaction(signedTx);
        });
        return new BetResult(true,(long) (amountReceived * multiplier), "ERG", txId);
    }

    public int getLastAddress() {
        Optional<LatestServerAddress> latestServerAddresses = latestServerAddressRepository.findById("1");
        int latestAddressUsed = latestServerAddresses.get().getAddress();
        return latestAddressUsed;
    }

    public BetResult checkFlipLost(String txId, String winnerAddress) {
        Long amountToSend = TransactionMapper.getAmountToSend(txId);
        addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress, txId, amountToSend, false));
        return new BetResult(false,0L, "ERG", txId);
    }
    private static String getDefaultNodeUrl(boolean mainNet) {
        return mainNet ? NODE_MAINNET : NODE_TESTNET;
    }
}
