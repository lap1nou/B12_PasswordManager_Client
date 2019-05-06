package ch.heigvd.pro.client.structure;

public class Password {
    private static int idGlobal = 0;
    private int id;
    private double strength;
    private String password;

    public Password(String password) {
        this.id = idGlobal++;
        this.password = password;
    }

    @Override
    public String toString() {
        return password;
    }

}