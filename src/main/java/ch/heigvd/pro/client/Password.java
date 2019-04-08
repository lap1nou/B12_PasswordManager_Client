package ch.heigvd.pro.client;

public class Password {
    private static int idGlobal = 0;
    private int id;
    private double strength;
    private String password;

    public Password(String password) {
        this.id = idGlobal++;
        this.password = password;
    }
}