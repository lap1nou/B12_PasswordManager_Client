package ch.heigvd.pro.client;

public class Password {
    private static int idGlobal = 0;
    private int id;
    private double strength;
    private String password;
    private byte[] iv;

    public Password(String password, byte[] iv) {
        this.id = idGlobal++;
        this.password = password;
        this.iv = iv;
    }

    @Override
    public String toString() {
        return password;
    }

    public byte[] getIv() {
        return iv;
    }
}