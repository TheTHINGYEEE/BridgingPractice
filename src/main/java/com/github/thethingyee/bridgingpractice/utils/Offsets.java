package com.github.thethingyee.bridgingpractice.utils;

import com.github.thethingyee.bridgingpractice.BridgingPractice;

public class Offsets {

    private static BridgingPractice bridgingPractice;

    public Offsets(BridgingPractice bridgingPractice) {
        this.bridgingPractice = bridgingPractice;
    }

    public static double[] getOffsets(String schematicName) {
        return new double[]{bridgingPractice.getConfig().getDouble("offset." + schematicName + ".x"), bridgingPractice.getConfig().getDouble("offset." + schematicName + ".y"), bridgingPractice.getConfig().getDouble("offset." + schematicName + ".z")};
    }

    public static int getRotation(String schematicName) {
        return bridgingPractice.getConfig().getInt("offset." + schematicName + ".rotation");
    }
}
