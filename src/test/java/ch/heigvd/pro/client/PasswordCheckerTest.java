package ch.heigvd.pro.client;

import ch.heigvd.pro.client.password.PasswordChecker;
import ch.heigvd.pro.client.structure.Entry;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordCheckerTest {

    @Test
    public void checkPasswordWithLowLength() {
        PasswordChecker checker = new PasswordChecker();
        Entry entry = new Entry("stefan".toCharArray(), "www.test.ch".toCharArray(), "Hel12@g".toCharArray(), "stefan.dejanovic@test.ch".toCharArray(), "salt".toCharArray(), new Date());
        int score = checker.checkStrong(entry);
        assertEquals(0, score);
    }


    @Test
    public void checkMaxPointScore() {
        PasswordChecker checker = new PasswordChecker();
        Entry entry = new Entry("stefan".toCharArray(), "www.test.ch".toCharArray(), "HHHabcdef#12sb*".toCharArray(), "stefan.dejanovic@test.ch".toCharArray(), "salt".toCharArray(), new Date());
        int score = checker.checkStrong(entry);
        assertEquals(100, score);
    }


    @Test
    public void checkIfMaxPointIs100() {
        PasswordChecker checker = new PasswordChecker();
        Entry entry = new Entry("stefan".toCharArray(), "www.test.ch".toCharArray(), "HHHabcdHHHef#12sb*#%&)(=".toCharArray(), "stefan.dejanovic@test.ch".toCharArray(), "salt".toCharArray(), new Date());
        int score = checker.checkStrong(entry);
        assertEquals(100, score);
    }
}
