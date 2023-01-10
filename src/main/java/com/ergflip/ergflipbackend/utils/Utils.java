package com.ergflip.ergflipbackend.utils;

public class Utils {

    public static double getMultiplier(int multiplier) {
        switch (multiplier) {
            case 2:
                return 1.95;
            case 6:
                return 5.8;
            case 10:
                return 9.7;
            case 3:
                return 2.9;
            default:
                return 0;
        }
    }
}