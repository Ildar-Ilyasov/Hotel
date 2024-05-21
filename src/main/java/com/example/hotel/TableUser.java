package com.example.hotel;

public class TableUser {
    private long id;
    private String name;
    private String login;
    private String password;
    private String passport;
    private long room;
    private long checkin;
    private long departure;

    public TableUser(long id, String name, String login, String password, String passport, long room, long checkin, long departure) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.passport = passport;
        this.room = room;
        this.checkin = checkin;
        this.departure = departure;

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
    public long getRoom() {
        return room;
    }
    public void setRoom(long room) {
        this.room = room;
    }
    public long getCheckin() {
        return checkin;
    }
    public void setChecking(long checkin) {
        this.checkin = checkin;
    }
    public long getDeparture() {
        return departure;
    }
    public void setDepartureg(long departure) {
        this.departure = departure;
    }
}
