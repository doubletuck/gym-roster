package com.gym.roster.domain;

public enum Conference {

    ACC,
    BIG12,
    BIGTEN,
    EAGL,
    GEC,
    IND,
    MAC,
    MIC,
    MPSF,
    MW,
    NCGAEAST,
    PAC12,
    SEC,
    WIAC;

    public static Conference find(String text) {

        if (text != null && !text.isEmpty()) {
            text = text.trim();
            for (Conference conference : Conference.values()) {
                if (conference.name().equalsIgnoreCase(text)) {
                    return conference;
                }
            }
        }
        return null;
    }
}
