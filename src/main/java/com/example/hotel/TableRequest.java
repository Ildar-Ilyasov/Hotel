package com.example.hotel;

public class TableRequest {
    private long id;
    private String name;
    private String entry;
    private String exit;
    private String classofroom;
    private String count;
    private String cost;
    private String room;

    public TableRequest(long id, String name, String entry, String exit, String classofroom, String count, String cost, String room) {
        this.id = id;
        this.name = name;
        this.entry = entry;
        this.exit = exit;
        this.classofroom = classofroom;
        this.count = count;
        this.cost = cost;
        this.room = room;

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
    public String getEntry() {
        return entry;
    }
    public void setEntry(String entry) {
        this.entry = entry;
    }
    public String getExit() {
        return exit;
    }
    public void setExit(String exit) {
        this.exit = exit;
    }
    public String getClassofroom() {
        return classofroom;
    }
    public void setClassofroom(String classofroom) {
        this.classofroom = classofroom;
    }
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
}
