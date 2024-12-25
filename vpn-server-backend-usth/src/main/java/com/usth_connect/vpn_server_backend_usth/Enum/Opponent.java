package com.usth_connect.vpn_server_backend_usth.Enum;

public enum Opponent {
    CHIT_CHAT("Chit-chatting"),
    SHARE_KNOWLEDGE("Share knowledge"),
    STUDY_SUPPORTER("Study Supporter");

    private final String displayValue;

    Opponent(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
