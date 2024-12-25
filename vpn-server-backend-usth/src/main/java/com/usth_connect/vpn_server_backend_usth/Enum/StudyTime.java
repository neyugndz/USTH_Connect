package com.usth_connect.vpn_server_backend_usth.Enum;

public enum StudyTime {
    EARLY_MORNING("Early Morning"),
    MIDDAY("Midday"),
    EVENING("Evening"),
    NIGHT("Night"),
    MIDNIGHT("Midnight"),
    OVERNIGHT("Overnight");

    private final String displayValue;

    StudyTime(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

