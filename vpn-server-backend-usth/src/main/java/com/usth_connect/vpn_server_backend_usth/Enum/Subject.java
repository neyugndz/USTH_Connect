package com.usth_connect.vpn_server_backend_usth.Enum;

public enum Subject {
    CALCULUS("Calculus"),
    LINEAR_ALGEBRA("Linear Algebra"),
    CHEMISTRY("Chemistry"),
    PHYSICS("Physics"),
    PROGRAMMING("Programming"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    BIOLOGY("Biology"),
    PRACTICAL_LAB("Practical laboratory"),
    ENVIRONMENT("Environment"),
    HEALTH_CARE("Health Care"),
    ELECTRONIC("Electronic");

    private final String displayValue;

    Subject(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
