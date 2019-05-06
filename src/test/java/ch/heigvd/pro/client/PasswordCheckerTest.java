package ch.heigvd.pro.client;

import ch.heigvd.pro.client.password.PasswordChecker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordCheckerTest {

    @Test
    public void checkPasswordWithLowLength() {
        int score = PasswordChecker.checkStrong("Hel12@g".toCharArray());
        assertEquals(0, score);
    }


    @Test
    public void checkMaxPointScore() {
        int score = PasswordChecker.checkStrong("HHHabcdef#12sb*".toCharArray());
        assertEquals(100, score);
    }


    @Test
    public void checkIfMaxPointIs100() {
        int score = PasswordChecker.checkStrong("HHHabcdHHHef#12sb*#%&)(=".toCharArray());
        assertEquals(100, score);
    }
}
