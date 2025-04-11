package com.gym.roster.domain;

public enum Region {

    C,
    NC,
    NE,
    SC,
    SE,
    W,
    NA;

    public static Region find(String text) {

        if (text != null && !text.isEmpty()) {
            text = text.trim();
            for (Region region : Region.values()) {
                if (region.name().equalsIgnoreCase(text)) {
                    return region;
                }
            }
        }
        return null;
    }
}
