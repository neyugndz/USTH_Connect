package com.usth_connect.vpn_server_backend_usth.Enum;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    PREFER_NOT_TO_SAY("Prefer not to say");

    private final String displayValue;

    Gender(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static Gender fromDisplayValue(String displayValue) {
        for (Gender gender : Gender.values()) {
            if (gender.getDisplayValue().equals(displayValue)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown display value: " + displayValue);
    }
}
