package com.ergflip.ergflipbackend.service;

import com.ergflip.ergflipbackend.entity.*;
import com.ergflip.ergflipbackend.repository.AddressesRepository;
import com.ergflip.ergflipbackend.repository.LatestServerAddressRepository;
import com.ergflip.ergflipbackend.repository.TxIdRepository;
import com.ergflip.ergflipbackend.utils.TransactionMapper;
import org.ergoplatform.appkit.*;
import org.ergoplatform.appkit.Address;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.ergflip.ergflipbackend.utils.RouletteUtils.*;
import static com.ergflip.ergflipbackend.utils.TokenUtils.*;

@Service
public class ReceiveRouletteBetService {

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private LatestServerAddressRepository latestServerAddressRepository;
    public static final String NODE_MAINNET = "http://213.239.193.208:9053/";
    public static final String NODE_TESTNET = "http://213.239.193.208:9052/";
    private static final String[] serverAddresses = {"9g9b9XdDBrvENHiR86cjVdrJxJ7KBDtWbwdQQJvsFqoTTJ5EYte","9fSuzs5tQUSaitGQhurYFfHJUBqQ4pz94BTvwQKL37vT81UcEij","9hkrzEE9TgWBjJn8oNG5FXP5bh5SjaDE6892QkwCfwP3hbXFmnw","9gc3Rb9noL3wd5tu9Fk1663WFF381C8SCkR8LCkJJ8HHXySn4jm"};

    private ReentrantLock mutex = new ReentrantLock();

    public RouletteBetResult receiveBet(RouletteBet bet) throws IllegalAccessException, ParseException, InterruptedException {
        String tokenId = bet.getTokenId();
        if (tokenId == null) {return startSpinning(bet.getTxId(), bet.getBettorAddress(), bet.getTokenId(), bet.getTableBet());}
        else return startTokenSpinning(bet.getTxId(), bet.getBettorAddress(), bet.getTokenId(), bet.getTableBet());
    }

    public RouletteBetResult startSpinning(String txId, String winnerAddress, String tokenId, tableBet tableBet) throws IllegalAccessException, ParseException, InterruptedException {
        SplittableRandom random = new SplittableRandom();
        int randomInt = random.nextInt(38);
        Long amountToSend = (long) calculatePayout(tableBet, randomInt, tokenId);
        if (amountToSend != 0L) return sendErgsToWinner(txId, winnerAddress, tokenId, tableBet, amountToSend, randomInt);
        else return notifyLossAfterConfirming(txId, winnerAddress, randomInt);
    }

    public RouletteBetResult startTokenSpinning(String txId, String winnerAddress, String tokenId, tableBet tableBet) throws IllegalAccessException, ParseException, InterruptedException {
        SplittableRandom random = new SplittableRandom();
        int randomInt = random.nextInt(38);
        Long amountToSend = (long) calculatePayout(tableBet, randomInt, tokenId);
        if (amountToSend != 0L) return sendTokensToWinner(txId, winnerAddress, tokenId, tableBet, amountToSend, randomInt);
        else return notifyLossTokenAfterConfirming(txId, winnerAddress, randomInt, tokenId);
    }

    public double calculatePayout(tableBet tableBet, int winningNumber, String tokenId) {
        Double amountToSend = 0.0;
        tableBetObject oneToEighteen = tableBet.getOneToEighteen();
        if (oneToEighteen != null && oneToEighteen.getValues().length > 0 && isBetweenRange(1,18,winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : oneToEighteen.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 2);
        }
        System.out.println("amountToSend after 1-18: " + amountToSend);
        tableBetObject nineteenToThirtySix = tableBet.getNineteenToThirtySix();
        if (nineteenToThirtySix != null && tableBet.getNineteenToThirtySix().getValues().length > 0 && isBetweenRange(19,36,winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : nineteenToThirtySix.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend +=  (amountToSendAux * 2);
            System.out.println("amounttosend at 19to36 spot: " + amountToSend);
        }
        System.out.println("amountToSend after 19-36: " + amountToSend);
        tableBetObject even = tableBet.getEven();
        if (even != null && even.getValues().length > 0 && isEven(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : even.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 2);
            System.out.println("amounttosend at even spot: " + amountToSend);
        }
        System.out.println("amountToSend after even: " + amountToSend);
        tableBetObject odd = tableBet.getOdd();
        if (odd != null && odd.getValues().length > 0 && isOdd(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : odd.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 2);
            System.out.println("amounttosend at odd spot: " + amountToSend);
        }
        System.out.println("amountToSend after odd: " + amountToSend);
        tableBetObject red = tableBet.getRed();
        if (red != null && red.getValues().length > 0 && isRed(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : red.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 2);
            System.out.println("amounttosend at red spot: " + amountToSend);
        }
        System.out.println("amountToSend after red: " + amountToSend);
        tableBetObject black = tableBet.getBlack();
        if (black != null && black.getValues().length > 0 && isBlack(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : black.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend +=(amountToSendAux * 2);
            System.out.println("amounttosend at black spot: " + amountToSend);
        }
        System.out.println("amountToSend after black: " + amountToSend);
        tableBetObject firstRow = tableBet.getFirst();
        if (firstRow != null && firstRow.getValues().length > 0 && isInFirstRow(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : firstRow.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 3);
            System.out.println("amounttosend at firstRow spot: " + amountToSend);
        }
        System.out.println("amountToSend after firstRow: " + amountToSend);
        tableBetObject secondRow = tableBet.getSecond();
        if (secondRow!= null && secondRow.getValues().length > 0 && isInSecondRow(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : secondRow.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 3);
            System.out.println("amounttosend at second row: " + amountToSend);

        }
        System.out.println("amountToSend after secondRow: " + amountToSend);
        tableBetObject thirdRow = tableBet.getThird();
        if (thirdRow!= null && thirdRow.getValues().length > 0 && isInThirdRow(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : thirdRow.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 3);
            System.out.println("amounttosend at third row: " + amountToSend);
        }
        System.out.println("amountToSend after thirdRow: " + amountToSend);

        tableBetObject firstTwelve = tableBet.getFirstTwelve();
        if (firstTwelve!= null && firstTwelve.getValues().length > 0 && isInFirstTwelve(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : firstTwelve.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 3);
            System.out.println("amounttosend at firstTwelve row: " + amountToSend);
        }
        System.out.println("amountToSend after firstTwelve: " + amountToSend);

        tableBetObject secondTwelve = tableBet.getSecondTwelve();
        if (secondTwelve!= null && secondTwelve.getValues().length > 0 && isInSecondTwelve(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : secondTwelve.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 3);
            System.out.println("amounttosend at secondTwelve row: " + amountToSend);
        }
        System.out.println("amountToSend after secondTwelve: " + amountToSend);

        tableBetObject thirdTwelve = tableBet.getThirdTwelve();
        if (thirdTwelve!= null && thirdTwelve.getValues().length > 0 && isInThirdTwelve(winningNumber)) {
            Double amountToSendAux = 0.0;
            for (String value : thirdTwelve.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }
            amountToSend += (amountToSendAux * 3);
            System.out.println("amounttosend at thirdTwelve row: " + amountToSend);
        }
        System.out.println("amountToSend after thirdTwelve: " + amountToSend);

        tableBetObject winningSpot = tableBet.getNumber(winningNumber);
        if (winningSpot != null && winningSpot.getValues().length > 0) {
            Double amountToSendAux = 0.0;
            for (String value : winningSpot.getValues()) {
                amountToSendAux += Double.parseDouble(value);
            }

            amountToSend += (amountToSendAux * 36);
            System.out.println("amounttosend at winning spot: " + amountToSend);
        }
        System.out.println("amountToSend after winningSpot: " + amountToSend);
        HashMap<tableBetObject, Integer> adjacents_multipliers = tableBet.getAdjacent(winningNumber);
        for (tableBetObject adjacent: adjacents_multipliers.keySet()) {
            if (adjacent != null && adjacent.getValues().length > 0) {
                Double amountToSendAux = 0.0;
                for (String value : adjacent.getValues()) {

                    amountToSendAux += Double.parseDouble(value);
                }
                double v = amountToSendAux * adjacents_multipliers.get(adjacent);
                System.out.println("we sum " + v + " to amounttosend : " + amountToSend);
                amountToSend += v;
                System.out.println("now amounttosend is: " + amountToSend);
            }
        }
        System.out.println(tokenId);
        System.out.println(amountToSend);
        return amountToSendGivenTokenId(amountToSend, tokenId);
    }

    public RouletteBetResult sendErgsToWinner(String txId, String winnerAddress,String tokenId, tableBet tableBet, Long amountToSend, int winningNumber) throws IllegalAccessException, ParseException, InterruptedException {
        Optional<LatestServerAddress> latestServerAddresses = latestServerAddressRepository.findById("1");
        int latestAddressUsed = latestServerAddresses.get().getAddress();
        if (latestAddressUsed == 4) {
            latestServerAddressRepository.save(new LatestServerAddress(1,"1"));
        }
        else latestServerAddressRepository.save(new LatestServerAddress(latestAddressUsed + 1,"1"));
        System.out.println("LOOKS LIKE THE AMOUNT TO SEND IS "+ amountToSend);
        String[] phrases = {phrase, phrase2, phrase3, phrase4};
        String password = "";

        ErgoClient ergoClient = RestApiErgoClient.create(getDefaultNodeUrl(true), NetworkType.MAINNET, "", RestApiErgoClient.getDefaultExplorerUrl(NetworkType.MAINNET));
        //address receiving the tx.
        Address recipient = Address.create(winnerAddress);
        //amount received to the winner
        Long amountReceived2 = getAmountSent(tableBet, tokenId);
        Long amountReceived1 = TransactionMapper.getAmountToSend(txId);
        System.out.println("AMOUNT RECEIVED 1 IS "+ amountReceived1);
        System.out.println("AMOUNT RECEIVED 2 IS "+ amountReceived2);
        System.out.println(amountToSend);
        if (amountReceived2.longValue() < (amountReceived1.longValue() - 10) || amountReceived2.longValue() > (amountReceived1.longValue() + 10)) {
            System.out.println("amount received 1 is not equal to amount received 2");
            System.out.println(amountReceived1.longValue());
            System.out.println(amountReceived2.longValue());
            return new RouletteBetResult(null,null, null, txId);
        }

        ergoClient.execute((BlockchainContext ctx) -> {
            ErgoContract contract = recipient.toErgoContract();
            //address sending the tx.
            Address ownerAddress = Address.create(serverAddresses[latestAddressUsed-1]);

            UnsignedTransaction unsignedTransaction = BoxOperations.createForSender(ownerAddress, ctx)
                    .withAmountToSpend(amountToSend)
                    .withInputBoxesLoader(new ExplorerAndPoolUnspentBoxesLoader().withAllowChainedTx(true))
                    .withFeeAmount(1100000L)
                    .putToContractTxUnsigned(contract);

            Mnemonic mn = Mnemonic.create(phrases[latestAddressUsed-1].toCharArray(), password.toCharArray());

            SignedTransaction signedTx = ctx.newProverBuilder().withMnemonic(mn).withEip3Secret(0).build().sign(unsignedTransaction);
            addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,signedTx.getId() , amountToSend , true));
            return ctx.sendTransaction(signedTx);
        });
        RouletteBetResult betResult = new RouletteBetResult((long)winningNumber,amountToSend, "ERG", txId);
        System.out.println(betResult.getResult());
        return betResult;


    }
    public RouletteBetResult sendTokensToWinner(String txId, String winnerAddress,String tokenId, tableBet tableBet, Long amountToSend, int winningNumber) throws IllegalAccessException, ParseException, InterruptedException {
        Optional<LatestServerAddress> latestServerAddresses = latestServerAddressRepository.findById("1");
        int latestAddressUsed = latestServerAddresses.get().getAddress();
        if (latestAddressUsed == 4) {
            latestServerAddressRepository.save(new LatestServerAddress(1,"1"));
        }
        else latestServerAddressRepository.save(new LatestServerAddress(latestAddressUsed + 1,"1"));
        String[] phrases = {phrase, phrase2, phrase3, phrase4};
        String password = "";

        ErgoClient ergoClient = RestApiErgoClient.create(getDefaultNodeUrl(true), NetworkType.MAINNET, "", RestApiErgoClient.getDefaultExplorerUrl(NetworkType.MAINNET));
        //address receiving the tx.
        Address recipient = Address.create(winnerAddress);
        //amount received to the winner
        Long amountReceived2 = getAmountSent(tableBet, tokenId);
        List<Long> amountAndDencimals = TransactionMapper.getTokenAmountToSend(txId, tokenId);
        Long amountReceived1 = amountAndDencimals.get(0);
        System.out.println("AMOUNT RECEIVED 1 IS "+ amountReceived1);
        System.out.println("AMOUNT RECEIVED 2 IS "+ amountReceived2);
        System.out.println(amountToSend);
        if (amountReceived1.longValue() != amountReceived2.longValue()) {
            System.out.println("WTF");
            System.out.println(amountReceived1.longValue());
            System.out.println(amountReceived2.longValue());
            return new RouletteBetResult(null,null, null, txId);
        }
        Long maximumToSend = maximumRouletteAmountToSendPerToken(tokenId);

        ergoClient.execute((BlockchainContext ctx) -> {
            ErgoContract contract = recipient.toErgoContract();
            //address sending the tx.
            Address ownerAddress = Address.create(serverAddresses[latestAddressUsed-1]);
            List<ErgoToken> ergoTokens = Arrays.asList(new ErgoToken(tokenId, (amountToSend)));

            UnsignedTransaction unsignedTransaction = BoxOperations.createForSender(ownerAddress, ctx)
                    .withAmountToSpend(Parameters.MinChangeValue)
                    .withTokensToSpend(ergoTokens)
                    .withInputBoxesLoader(new ExplorerAndPoolUnspentBoxesLoader().withAllowChainedTx(true))
                    .withFeeAmount(1100000L)
                    .putToContractTxUnsigned(contract);

            Mnemonic mn = Mnemonic.create(phrases[latestAddressUsed-1].toCharArray(), password.toCharArray());

            SignedTransaction signedTx = ctx.newProverBuilder().withMnemonic(mn).withEip3Secret(0).build().sign(unsignedTransaction);
            addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress,signedTx.getId() , amountToSend , true));
            return ctx.sendTransaction(signedTx);
        });
        RouletteBetResult betResult = new RouletteBetResult((long)winningNumber,amountToSend,fromTokenIdToName(tokenId), txId);
        System.out.println(betResult.getResult());
        return betResult;

    }

    public RouletteBetResult notifyLossAfterConfirming(String txId, String winnerAddress, int winningNumber) throws ParseException, InterruptedException {
        Long amountToSend = TransactionMapper.getAmountToSend(txId);
        addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress, txId, amountToSend, false));
        return new RouletteBetResult((long)winningNumber, 0L, "ERG", txId);
    }

    public RouletteBetResult notifyLossTokenAfterConfirming(String txId, String winnerAddress, int winningNumber, String tokenId) {
        List<Long> amountAndDecimals = TransactionMapper.getTokenAmountToSend(txId, tokenId);
        Long amountToSend = amountAndDecimals.get(0);
        addressesRepository.save(new com.ergflip.ergflipbackend.entity.Address(winnerAddress, txId, amountToSend, false));
        return new RouletteBetResult((long)winningNumber, 0L, fromTokenIdToName(tokenId), txId);
    }

    private static String getDefaultNodeUrl(boolean mainNet) {
        return mainNet ? NODE_MAINNET : NODE_TESTNET;
    }
}