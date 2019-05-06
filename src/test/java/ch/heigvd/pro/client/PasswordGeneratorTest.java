package ch.heigvd.pro.client;

import ch.heigvd.pro.client.password.PasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {
    @Test
    public void generatePasswordLengthTest() {
        // Source : https://stackoverflow.com/questions/17575840/better-way-to-generate-array-of-all-letters-in-the-alphabet
        char[] charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"#$%&')(*+,-_./".toCharArray();

        PasswordGenerator test = new PasswordGenerator(charSet, 6);
        char[] result = test.generatePassword();

        assertEquals(result.length, 6);
    }

}