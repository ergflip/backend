package com.ergflip.ergflipbackend.utils;

import org.ergoplatform.appkit.*;
import org.ergoplatform.appkit.impl.ErgoTreeContract;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ErgoPayUtils {
    public static final String NODE_MAINNET = "http://213.239.193.208:9053/";
    public static final String NODE_TESTNET = "http://213.239.193.208:9052/";

    public static ReducedTransaction getReducedSendTx(boolean isMainNet, long amountToSend, Address sender, Address recipient) {
        NetworkType networkType = isMainNet ? NetworkType.MAINNET : NetworkType.TESTNET;
        return RestApiErgoClient.create(
                getDefaultNodeUrl(isMainNet),
                networkType,
                "",
                RestApiErgoClient.getDefaultExplorerUrl(networkType)
        ).execute(ctx -> {
            ErgoContract contract = recipient.toErgoContract();
            UnsignedTransaction unsignedTransaction = BoxOperations.createForSender(sender, ctx)
                    .withAmountToSpend(amountToSend)
                    .withInputBoxesLoader(new ExplorerAndPoolUnspentBoxesLoader().withAllowChainedTx(true))
                    .putToContractTxUnsigned(contract);

            return ctx.newProverBuilder().build().reduce(unsignedTransaction, 0);
        });
    }

    public static ReducedTransaction getReducedTokensSendTx(boolean isMainNet, long amountToSend, Address sender, Address recipient, String tokenId) {
        NetworkType networkType = isMainNet ? NetworkType.MAINNET : NetworkType.TESTNET;
        return RestApiErgoClient.create(
                getDefaultNodeUrl(isMainNet),
                networkType,
                "",
                RestApiErgoClient.getDefaultExplorerUrl(networkType)
        ).execute(ctx -> {
            ErgoContract contract = recipient.toErgoContract();

            List<ErgoToken> ergoTokens = Arrays.asList(new ErgoToken(tokenId, amountToSend));
            UnsignedTransaction unsignedTransaction = BoxOperations.createForSender(sender, ctx)
                    .withAmountToSpend(Parameters.MinChangeValue)
                    .withTokensToSpend(ergoTokens)
                    .withInputBoxesLoader(new ExplorerAndPoolUnspentBoxesLoader().withAllowChainedTx(true))
                    .withFeeAmount(2200000L)
                    .putToContractTxUnsigned(contract);

            return ctx.newProverBuilder().build().reduce(unsignedTransaction, 0);
        });
    }


    private static String getDefaultNodeUrl(boolean mainNet) {
        return mainNet ? NODE_MAINNET : NODE_TESTNET;
    }

}
