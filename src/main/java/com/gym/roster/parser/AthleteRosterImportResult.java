package com.gym.roster.parser;

import com.gym.roster.domain.AthleteRoster;
import lombok.Data;

@Data
public class AthleteRosterImportResult {

    public enum Status {
        CREATED,
        UPDATED,
        EXISTS,
        ERROR;
    }

    private Long recordNumber;
    private Status athleteImportStatus;
    private Status rosterImportStatus;
    private AthleteRoster roster;
    private String message;
}
