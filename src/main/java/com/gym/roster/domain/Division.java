package com.gym.roster.domain;

public enum Division {
    DIV1,
    DIV2,
    DIV3;

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
