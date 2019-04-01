package ch.heigvd.pro.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoTest {
    @Test
    public void fiveAnonimityTest(){
        Crypto.fiveAnonimitySHA1("Test");
        assertEquals(0,1);
    }
}