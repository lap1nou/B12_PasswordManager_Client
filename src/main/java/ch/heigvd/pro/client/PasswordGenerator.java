package ch.heigvd.pro.client;

import java.security.SecureRandom;

public class PasswordGenerator {
    private char[] charSet;
    private Integer size;

    public PasswordGenerator(char[] charSet, Integer size) {
        this.charSet = charSet;
        this.size = size;
    }

    public String generatePassword() {
        StringBuilder result = new StringBuilder();
        SecureRandom index = new SecureRandom();

        for (int i = 0; i < size; i++) {
            result.append(charSet[index.nextInt(charSet.length)]);
        }

        return result.toString();
    }
}
