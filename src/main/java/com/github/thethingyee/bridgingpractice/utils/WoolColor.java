package com.github.thethingyee.bridgingpractice.utils;

public class WoolColor {

    public static int woolToDye(short durability) {
        switch(durability) {
            case 0: {
                return 15;
            }
            case 1: {
                return 14;
            }
            case 2: {
                return 13;
            }
            case 3: {
                return 12;
            }
            case 4: {
                return 11;
            }
            case 5: {
                return 10;
            }
            case 6: {
                return 9;
            }
            case 7: {
                return 8;
            }
            case 8: {
                return 7;
            }
            case 9: {
                return 6;
            }
            case 10: {
                return 5;
            }
            case 11: {
                return 4;
            }
            case 12: {
                return 3;
            }
            case 13: {
                return 2;
            }
            case 14: {
                return 1;
            }
            case 15: {
                return 0;
            }
        }
        return 0;
    }
}
