package com.usth_connect.vpn_server_backend_usth.Enum;

public enum Interest {
    V_POP("V-pop"),
    K_POP("K-pop"),
    EDM("EDM"),
    ART("Art"),
    POLITICS("Politics"),
    VLOGGING("Vlogging"),
    BASEBALL("Baseball"),
    FOOTBALL("Football"),
    VOLLEYBALL("Volleyball"),
    TABLE_TENNIS("Table tennis"),
    BASKETBALL("Basketball"),
    SKIING("Skiing"),
    SWIMMING("Swimming"),
    SKATEBOARDING("Skateboarding"),
    BOARD_GAMES("Board games"),
    DIY("DIY"),
    COOKING("Cooking"),
    PAINTING("Painting"),
    BOWLING("Bowling"),
    BALLET("Ballet"),
    DANCING("Dancing"),
    SINGING("Singing"),
    YOGA("Yoga"),
    JOGGING("Jogging"),
    ANIME("Anime"),
    FOOD_TOUR("Food tour"),
    TRAVEL("Travel"),
    STUDY("Study"),
    GYM("Gym"),
    KOREAN_FOOD("Korean food"),
    COFFEE("Coffee"),
    TEA("Tea"),
    BADMINTON("Badminton"),
    HIPHOP("Hiphop"),
    INVESTMENT("Investment"),
    ALCOHOLIC("Alcoholic"),
    KARATE("Karate"),
    TAEKWONDO("Taekwondo"),
    RAP("Rap"),
    MAKEUP("Makeup"),
    MOVIE("Movie"),
    NETFLIX("Netflix"),
    ICE_CREAM("Ice cream"),
    ESPORT("Esport");

    private final String displayValue;

    Interest(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

