package com.gym.roster.parser;

import com.gym.roster.domain.CoachRoster;
import lombok.Data;

@Data
public class CoachRosterImportResult {

    public enum Status {
        CREATED,
        UPDATED,
        EXISTS,
        ERROR;
    }

    private Long recordNumber;
    private Status coachImportStatus;
    private Status rosterImportStatus;
    private CoachRoster roster;
    private String message;
}
