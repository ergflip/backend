package com.ergflip.ergflipbackend.utils;

public class TokenUtils {

    public static Long maximumAmountToBetPerToken(String tokenId) {
        switch (tokenId) {
            case "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04":
                return 500L;
            case "0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b":
                return 400000L;
            case "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f":
                return 400000L;
            default:
                return null;
        }
    }

    public static Long maximumRouletteAmountToSendPerToken(String tokenId) {
        switch (tokenId) {
            case "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04":
                return 25000L;
            case "0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b":
                return 1500000L;
            case "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f":
                return 15000000L;
            default:
                return null;
        }
    }


    public static Long maximumAmountToSendPerToken(String tokenId) {
        switch (tokenId) {
            case "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04":
                return 500L;
            case "0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b":
                return 400000L;
            case "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f":
                return 400000L;
            default:
                return null;
        }
    }

    public static String fromTokenIdToName(String tokenId) {
        switch (tokenId) {
            case "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04":
                return "SigUSD";
            case "0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b":
                return "COMET";
            case "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f":
                return "CYPX";
            default:
                return null;
        }
    }
}
