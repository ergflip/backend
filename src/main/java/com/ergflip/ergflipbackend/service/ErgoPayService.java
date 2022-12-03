package com.ergflip.ergflipbackend.service;

import com.ergflip.ergflipbackend.entity.ErgoPayResponse;
import com.ergflip.ergflipbackend.entity.ServerAddress;
import com.ergflip.ergflipbackend.entity.TxId;
import com.ergflip.ergflipbackend.repository.AddressesRepository;
import com.ergflip.ergflipbackend.repository.LastServerAddressRepository;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import org.ergoplatform.appkit.Address;
import org.ergoplatform.appkit.ReducedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ergflip.ergflipbackend.utils.ErgoPayUtils;

import java.util.Base64;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static com.ergflip.ergflipbackend.utils.ErgoPayUtils.getReducedSendTx;
import static com.ergflip.ergflipbackend.utils.ErgoPayUtils.getReducedTokensSendTx;
import static com.ergflip.ergflipbackend.utils.TokenUtils.maximumAmountToBetPerToken;

@Service
public class ErgoPayService {
    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private TxIdRepository txIdRepository;

    @Autowired
    private LastServerAddressRepository lastServerAddressRepository;

    private ReentrantLock mutex = new ReentrantLock();

    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};

    public ErgoPayResponse roundTrip(String address, Long amount) {
        ErgoPayResponse response = new ErgoPayResponse();
        if (amount > 5000000000L || amount == 0L) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = "Amount must be between 1 and 5 ERGs";
            return response;
        }
        try {
            boolean isMainNet = true;
            Address sender = Address.create(address);
            try {
                mutex.lock();
                List<ServerAddress> latestServerAddresses = lastServerAddressRepository.findAll();
                int latestAddressUsed = latestServerAddresses.get(0).getAddress();
                lastServerAddressRepository.deleteAll();
                if (latestAddressUsed == 4) {
                    lastServerAddressRepository.save(new ServerAddress(1));
                }
                else lastServerAddressRepository.save(new ServerAddress(latestAddressUsed + 1));

                Address recipient = Address.create(serverAddresses[latestAddressUsed-1]);

                ReducedTransaction unsignedTx = getReducedSendTx(isMainNet, amount, sender, recipient);
                byte[] reduced = unsignedTx.toBytes();
                String txId = unsignedTx.getId();

                response.reducedTx = Base64.getUrlEncoder().encodeToString(reduced);
                response.address = address;
                response.message = "Bet to sign:";
                response.messageSeverity = ErgoPayResponse.Severity.INFORMATION;
                response.replyTo = "https://backend-url/receiveErgopayBet/"+address+"/"+txId;
            } finally {
                mutex.unlock();
            }




        } catch (Throwable t) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = (t.getMessage());
        }
        return response;
    }

    public ErgoPayResponse roundTokenTrip(String address, Long amount, String tokenId) {
        ErgoPayResponse response = new ErgoPayResponse();
        if (amount > maximumAmountToBetPerToken(tokenId) || amount == 0L) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = "Amount selected isn't valid for this token";
            return response;
        }
        try {
            boolean isMainNet = true;
            Address sender = Address.create(address);

            try {
                mutex.lock();

                List<ServerAddress> latestServerAddresses = lastServerAddressRepository.findAll();
                int latestAddressUsed = latestServerAddresses.get(0).getAddress();
                lastServerAddressRepository.deleteAll();
                if (latestAddressUsed == 4) {
                    lastServerAddressRepository.save(new ServerAddress(1));
                }
                else lastServerAddressRepository.save(new ServerAddress(latestAddressUsed + 1));

                Address recipient = Address.create(serverAddresses[latestAddressUsed-1]);
                ReducedTransaction unsignedTx = getReducedTokensSendTx(isMainNet, amount, sender, recipient, tokenId);
                byte[] reduced = unsignedTx.toBytes();
                String txId = unsignedTx.getId();

                response.reducedTx = Base64.getUrlEncoder().encodeToString(reduced);
                response.address = address;
                response.message = "Bet to sign:";
                response.messageSeverity = ErgoPayResponse.Severity.INFORMATION;
                response.replyTo = "https://backend-url/receiveErgopayBet/"+address+"/"+txId+"/"+tokenId;
            } finally {
                mutex.unlock();
            }


        } catch (Throwable t) {
            response.messageSeverity = ErgoPayResponse.Severity.ERROR;
            response.message = (t.getMessage());
        }
        return response;
    }

}
