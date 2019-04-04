package ch.heigvd.pro.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaveIBeenPwnedTest {
    @Test
    public void leakedPasswordTest() {
        String leakedPassword = "1234";
        String notLeakedPassword = "24adadfgdGGDhT576SSG1ç%41BBB//@@|#|";

        HaveIBeenPwned test = new HaveIBeenPwned();

        assertTrue(test.isLeaked(leakedPassword));
        assertFalse(test.isLeaked(notLeakedPassword));
    }

}