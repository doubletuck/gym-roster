package com.gym.roster.domain;

import lombok.Getter;

@Getter
public enum Division {
    DIV1("I", "Division I"),
    DIV2("II", "Division II"),
    DIV3("III", "Division III");

    private final String shortName;
    private final String longName;

    Division(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public static Division find(String text) {

        if (text != null && !text.isEmpty()) {
            text = text.trim();
            for (Division division : Division.values()) {
                if (division.name().equalsIgnoreCase(text)) {
                    return division;
                }
            }
        }
        return null;
    }
}
