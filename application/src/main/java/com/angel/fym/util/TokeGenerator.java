package com.angel.fym.util;

import java.util.Random;

public class TokeGenerator {
    public static String generateToken() {
        String salt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder builder = new StringBuilder();
        Random rnd = new Random();
        while (builder.length() < 18) {
            int index = (int) (rnd.nextFloat() * salt.length());
            builder.append(salt.charAt(index));
        }
        String saltStr = builder.toString();
        return saltStr;
    }
}
