package com.example.hotel;

public class Session {
    private static Long currentUserId;
    private static Long BookingId;

    public static void setCurrentUserId(Long user) {
        currentUserId = user;
    }

    public static Long getCurrentUserId() {
        return currentUserId;
    }
}
