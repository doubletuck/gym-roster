package com.gym.roster.domain;

import lombok.Getter;

@Getter
public enum Conference {

    ACC("ACC", "Atlantic Coast Conference"),
    BIG12("Big 12", "Big 12 Conference"),
    BIGTEN("Big Ten", "Big Ten Conference"),
    EAGL("EAGL", "Eastern Atlantic Gymnastics League"),
    GEC("GEC", "Gymnastics East Cooperative"),
    IND("IND", "Independent"),
    MAC("MAC", "Mid-American Conference"),
    MIC("MIC", "Midwest Independents Conference"),
    MPSF("MPSF", "Mountain Pacific Sports Federation"),
    MW("MW", "Mountain West Conference"),
    NCGAEAST("NCGA East", "National Collegiate Gymnastics Association East"),
    PAC12("Pac-12", "Pac-12 Conference"),
    SEC("SEC", "Southeastern Conference"),
    WIAC("WIAC", "Wisconsin Intercollegiate Athletic Conference");

    private final String shortName;
    private final String longName;

    Conference(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

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
