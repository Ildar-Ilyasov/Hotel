package com.example.hotel;

public class TableRequest {
    private long id;
    private String userName;
    private String arrivalDate;
    private String departureDate;
    private String quality;
    private String amountPeople;
    private String cost;
    private String roomNumber;

    public TableRequest(long id, String userName, String arrivalDate, String departureDate, String quality, String amountPeople, String cost, String roomNumber) {
        this.id = id;
        this.userName = userName;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.quality = quality;
        this.amountPeople = amountPeople;
        this.cost = cost;
        this.roomNumber = roomNumber;

    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return userName;
    }
    public void setName(String userName) {
        this.userName = userName;
    }
    public String getEntry() {
        return arrivalDate;
    }
    public void setEntry(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    public String getExit() {
        return departureDate;
    }
    public void setExit(String departureDate) {
        this.departureDate = departureDate;
    }
    public String getQuality() {
        return quality;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public String getCount() {
        return amountPeople;
    }
    public void setCount(String amountPeople) {
        this.amountPeople = amountPeople;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public String getRoom() {
        return roomNumber;
    }
    public void setRoom(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
