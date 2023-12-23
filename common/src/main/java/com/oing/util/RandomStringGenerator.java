package com.oing.util;

import java.util.Random;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 12/21/23
 * Time: 7:07 PM
 */
public class RandomStringGenerator {
    public static String generateAlphanumericString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
