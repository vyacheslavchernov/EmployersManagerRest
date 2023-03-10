package com.vych.EmployersManagerRest;

import java.util.Random;

public class TestUtils {
    private static final Random r = new Random();

    public static String getRandomLettersStringWithLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (r.nextInt(26) + 'a');
            sb.append(c);
        }

        return sb.toString();
    }

    public static String getRandomNumbersStringWithLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (r.nextInt(9) + '0');
            sb.append(c);
        }

        return sb.toString();
    }

    public static String getRandomBitsWithLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (r.nextInt(2) + '0');
            sb.append(c);
        }

        return sb.toString();
    }
}
