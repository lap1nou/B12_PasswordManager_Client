package ch.heigvd.pro.client;

import java.util.Date;

public class Entry {
    private static int idGlobal = 0;
    private int id;
    private String username;
    private String target;
    private Password password;
    private String email;
    private Date registerDate;

    public Entry(String username, String target, Password password, String email, Date registerDate) {
        this.id = idGlobal++;
        this.username = username;
        this.target = target;
        this.password = password;
        this.email = email;
        this.registerDate = registerDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

}