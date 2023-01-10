package com.ergflip.ergflipbackend.service;

import com.ergflip.ergflipbackend.entity.ErgoPayResponse;
import com.ergflip.ergflipbackend.entity.LatestServerAddress;
import com.ergflip.ergflipbackend.entity.ServerAddress;
import com.ergflip.ergflipbackend.entity.TxId;
import com.ergflip.ergflipbackend.repository.AddressesRepository;
import com.ergflip.ergflipbackend.repository.LastServerAddressRepository;
import com.ergflip.ergflipbackend.repository.LatestServerAddressRepository;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import org.ergoplatform.appkit.Address;
import org.ergoplatform.appkit.ReducedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ergflip.ergflipbackend.utils.ErgoPayUtils;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static com.ergflip.ergflipbackend.utils.ErgoPayUtils.getReducedSendTx;
import static com.ergflip.ergflipbackend.utils.ErgoPayUtils.getReducedTokensSendTx;
import static com.ergflip.ergflipbackend.utils.TokenUtils.maximumAmountToBetPerToken;

@Service
public class ErgoPayService {
    @Autowired
    private LatestServerAddressRepository latestServerAddressRepository;
    private ReentrantLock mutex = new ReentrantLock();
    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};

    public ErgoPayResponse roundTrip(String address, Long amount, String multiplier, String game) {
        ErgoPayResponse response = new ErgoPayResponse();
        if (amount > 11000000000L || amount == 0L) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = "Amount must be between 1 and 11 ERGs";
            return response;
        }
        try{
        boolean isMainNet = true;
        Address sender = Address.create(address);
        Optional<LatestServerAddress> latestServerAddresses = latestServerAddressRepository.findById("1");
        int latestAddressUsed = latestServerAddresses.get().getAddress();
        if (latestAddressUsed == 4) {
            latestServerAddressRepository.save(new LatestServerAddress(1,"1"));
        }
        else latestServerAddressRepository.save(new LatestServerAddress(latestAddressUsed + 1,"1"));
        Address recipient = Address.create(serverAddresses[latestAddressUsed-1]);

        ReducedTransaction unsignedTx = getReducedSendTx(isMainNet, amount, sender, recipient);
        byte[] reduced = unsignedTx.toBytes();
        String txId = unsignedTx.getId();

        response.reducedTx = Base64.getUrlEncoder().encodeToString(reduced);
        response.address = address;
        response.message = "Bet to sign:";
        response.messageSeverity = ErgoPayResponse.Severity.INFORMATION;
        response.replyTo = "https://ergflip-backend-production.up.railway.app/receiveErgopayBet/"+address+"/"+txId+"/"+multiplier+"/"+game;
    } catch (Throwable t) {
        response.messageSeverity = ErgoPayResponse.Severity.ERROR;
        response.message = (t.getMessage());
    }
        return response;
}

    public ErgoPayResponse roundTokenTrip(String address, Long amount, String tokenId, String multiplier, String game) {
        ErgoPayResponse response = new ErgoPayResponse();
        if (amount > maximumAmountToBetPerToken(tokenId) || amount == 0L) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = "Amount selected isn't valid for this token";
            return response;
        }
        try {
            boolean isMainNet = true;
            Address sender = Address.create(address);

            Optional<LatestServerAddress> latestServerAddresses = latestServerAddressRepository.findById("1");
            int latestAddressUsed = latestServerAddresses.get().getAddress();
            if (latestAddressUsed == 4) {
                latestServerAddressRepository.save(new LatestServerAddress(1,"1"));
            }
            else latestServerAddressRepository.save(new LatestServerAddress(latestAddressUsed + 1,"1"));

            Address recipient = Address.create(serverAddresses[latestAddressUsed-1]);
            ReducedTransaction unsignedTx = getReducedTokensSendTx(isMainNet, amount, sender, recipient, tokenId);
            byte[] reduced = unsignedTx.toBytes();
            String txId = unsignedTx.getId();

            response.reducedTx = Base64.getUrlEncoder().encodeToString(reduced);
            response.address = address;
            response.message = "Bet to sign:";
            response.messageSeverity = ErgoPayResponse.Severity.INFORMATION;
            response.replyTo = "https://ergflip-backend-production.up.railway.app/receiveErgopayBet/"+address+"/"+txId+"/"+tokenId+"/"+multiplier+"/"+game;

        } catch (Throwable t) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = (t.getMessage());
        }
        return response;
    }

    public ErgoPayResponse roundRouletteTrip(String address, Long amount, String tokenId) {
        ErgoPayResponse response = new ErgoPayResponse();
        try {
            boolean isMainNet = true;
            Address sender = Address.create(address);
            Optional<LatestServerAddress> latestServerAddresses = latestServerAddressRepository.findById("1");
            int latestAddressUsed = latestServerAddresses.get().getAddress();
            if (latestAddressUsed == 4) {
                latestServerAddressRepository.save(new LatestServerAddress(1,"1"));
            }
            else latestServerAddressRepository.save(new LatestServerAddress(latestAddressUsed + 1,"1"));

            Address recipient = Address.create(serverAddresses[latestAddressUsed-1]);
            ReducedTransaction unsignedTx = null;
            System.out.println(tokenId);
            System.out.println(amount);
            System.out.println(sender);
            System.out.println(recipient);
            if (!tokenId.equals("null")) {
                unsignedTx = getReducedTokensSendTx(isMainNet, amount, sender, recipient, tokenId);
            }
            else {
                unsignedTx = getReducedSendTx(isMainNet, amount, sender, recipient);
            }
            System.out.println("i get here");
            byte[] reduced = unsignedTx.toBytes();
            String txId = unsignedTx.getId();

            response.reducedTx = Base64.getUrlEncoder().encodeToString(reduced);
            response.address = address;
            response.message = "Bet to sign:";
            response.messageSeverity = ErgoPayResponse.Severity.INFORMATION;
            response.replyTo = "https://ergflip-backend-production.up.railway.app/receiveRouletteErgopayBet/"+address+"/"+txId;

        } catch (Throwable t) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = (t.getMessage());
        }
        return response;
    }
}
