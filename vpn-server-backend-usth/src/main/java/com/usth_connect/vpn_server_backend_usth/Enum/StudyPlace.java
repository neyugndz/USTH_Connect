package com.usth_connect.vpn_server_backend_usth.Enum;

public enum StudyPlace {
    CAFE("Café"),
    HOME("Home"),
    LIBRARY("Library"),
    BOOKSTORE("BookStore"),
    ONLINE("Online");

    private final String displayValue;

    StudyPlace(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

