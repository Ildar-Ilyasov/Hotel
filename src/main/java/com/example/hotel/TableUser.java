package com.example.hotel;

public class TableUser {
    private long id;
    private String name;
    private String login;
    private String password;
    private String passport;

    public TableUser(long id, String name, String login, String password, String passport) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.passport = passport;

    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassport() {
        return passport;
    }
    public void setPassport(String passport) {
        this.passport = passport;
    }
}
