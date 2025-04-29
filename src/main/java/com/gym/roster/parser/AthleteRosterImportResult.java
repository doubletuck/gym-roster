package com.gym.roster.parser;

import com.gym.roster.domain.Roster;
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
    private Roster roster;
    private String message;
}
