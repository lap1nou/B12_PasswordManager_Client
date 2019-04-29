package ch.heigvd.pro.client;

import java.security.SecureRandom;

public class PasswordGenerator {
    private char[] charSet;
    private Integer size;

    public PasswordGenerator(char[] charSet, Integer size) {
        this.charSet = charSet;
        this.size = size;
    }

    /**
     * Generate a random password based on size and charset attributes.
     *
     * @return a random password
     */
    public char[] generatePassword() {
        char[] result = new char[this.size];

        SecureRandom index = new SecureRandom();

        for (int i = 0; i < size; i++) {
            result[i] = charSet[index.nextInt(charSet.length)];
            System.out.print(result[i]);
        }

        // TODO : Checkez le mot de passe avec le module password checker
        return result;
    }
}