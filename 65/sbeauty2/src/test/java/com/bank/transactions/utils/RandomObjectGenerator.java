package com.bank.transactions.utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class RandomObjectGenerator {
    private RandomObjectGenerator() {
        // helper class constructor
    }

    public static String getRandomString() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
