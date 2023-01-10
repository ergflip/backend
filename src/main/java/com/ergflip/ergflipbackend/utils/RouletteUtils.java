package com.ergflip.ergflipbackend.utils;

import com.ergflip.ergflipbackend.entity.tableBet;
import com.ergflip.ergflipbackend.entity.tableBetObject;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class RouletteUtils {

    public static boolean isBetweenRange(int start, int end, int winningNumber) {
        if (start <= winningNumber && winningNumber <= end) {
            return true;
        }
        return false;
    }

    public static boolean isEven(int number) {
        if (number == 0 || number == 37) return false;
        if (number % 2 == 0) {
            return true;
        }
        return false;
    }

    public static boolean isOdd(int number) {
        if (number == 0 || number == 37) return false;
        if (number % 2 != 0) {
            return true;
        }
        return false;
    }

    public static double amountToSendGivenTokenId(double amountToSend, String tokenId) {
        if (tokenId == null) { return amountToSend * 1000000000;}
        else {
            switch (tokenId) {
                case "03faf2cb329f2e90d6d23b58d91bbb6c046aa143261cc21f52fbe2824bfcbf04":
                    return amountToSend * 100;
                case "0cd8c9f416e5b1ca9f986a7f10a84191dfb85941619e49e53c0dc30ebf83324b":
                    return amountToSend;
                case "01dce8a5632d19799950ff90bca3b5d0ca3ebfa8aaafd06f0cc6dd1e97150e7f":
                    return amountToSend  * 10000;
                default:
                    return  amountToSend;
            }
        }
    }
    public static boolean isRed(int number) {
        if (number == 0 || number == 37) return false;
        int[] redsArray = {1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36};
        for (int i = 0; i < redsArray.length; i++) {
            if (redsArray[i] == number) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlack(int number) {
        if (number == 0 || number == 37) return false;
        int[] blacksArray = {2,4,6,8,10,11,13,15,17,20,22,24,26,28,29,31,33,35};
        for (int i = 0; i < blacksArray.length; i++) {
            if (blacksArray[i] == number) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInFirstRow(int number) {
        switch (number) {
            case 1:
            case 4:
            case 7:
            case 10:
            case 13:
            case 16:
            case 19:
            case 22:
            case 25:
            case 28:
            case 31:
            case 34:
                return true;
            default:
                return false;
        }
    }

    public static boolean isInSecondRow(int number) {
        switch (number) {
            case 2:
            case 5:
            case 8:
            case 11:
            case 14:
            case 17:
            case 20:
            case 23:
            case 26:
            case 29:
            case 32:
            case 35:
                return true;
            default:
                return false;
        }
    }

    public static boolean isInThirdRow(int number) {
        switch (number) {
            case 3:
            case 6:
            case 9:
            case 12:
            case 15:
            case 18:
            case 21:
            case 24:
            case 27:
            case 30:
            case 33:
            case 36:
                return true;
            default:
                return false;
        }
    }

    public static boolean isInFirstTwelve(int number) {
        if (number >= 1 && number <= 12) {
            return true;
        }
        return false;
    }

    public static boolean isInSecondTwelve(int number) {
        if (number >= 13 && number <= 24) {
            return true;
        }
        return false;
    }

    public static boolean isInThirdTwelve(int number) {
        if (number >= 25 && number <= 36) {
            return true;
        }
        return false;
    }

    public static Long getAmountSent(tableBet tableBet, String tokenId) throws IllegalAccessException {
        Field[] fields = tableBet.getClass().getDeclaredFields();
        double amountToSend = 0L;
        for(Field f : fields){
            tableBetObject v =(tableBetObject) f.get(tableBet);
            if (v != null && v.getValues().length > 0) {
                for (String value : v.getValues()) {
                    System.out.println("we are going to sum " + value);
                    amountToSend += Double.parseDouble(value);
                }
            }
        }
        return (long) (amountToSendGivenTokenId(amountToSend, tokenId));
    }
}