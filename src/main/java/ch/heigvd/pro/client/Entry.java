package ch.heigvd.pro.client;

import java.util.Date;

public class Entry {
    private static int idGlobal = 0;
    private int id;
    private char[] username;
    private char[] target;
    private transient char[] clearPassword;
    private Password password;
    private char[] email;
    private Date registerDate;

    public Entry(char[] username, char[] target, char[] clearPassword, char[] email, Date registerDate) {
        this.id = idGlobal++;
        this.username = username;
        this.target = target;
        this.clearPassword = clearPassword;
        this.email = email;
        this.registerDate = registerDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char[] getUsername() {
        return username;
    }

    public void setUsername(char[] username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public char[] getEmail() {
        return email;
    }

    public void setEmail(char[] email) {
        this.email = email;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public char[] getClearPassword() {
        return clearPassword;
    }

    public char[] getTarget() {
        return target;
    }

    public void setClearPassword(char[] clearPassword) {
        this.clearPassword = clearPassword;
    }
}