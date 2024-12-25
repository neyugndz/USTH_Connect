package com.usth_connect.vpn_server_backend_usth.Enum;

public enum CommunicationStyle {
    VIDEO_CALL("Video call"),
    PHONE_CALL("Phone call"),
    TEXT_MESSAGE("Text message"),
    IN_PERSON("In person (face to face)");

    private final String displayValue;

    CommunicationStyle(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

