package de.haukesomm.sokoban.game;

import java.util.Random;

public class Id {

    private static final char[] CHARS = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    public static String next(int length) {
        var idBuilder = new StringBuilder();
        var random = new Random();

        for (int i = 0; i < length; i++) {
            idBuilder.append(CHARS[random.nextInt(length)]);
        }

        return idBuilder.toString();
    }

    public static String next() {
        return next(6);
    }

    private Id() {}
}
