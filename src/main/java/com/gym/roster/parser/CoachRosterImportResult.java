package com.gym.roster.parser;

import com.gym.roster.domain.CoachRoster;
import lombok.Data;

@Data
public class CoachRosterImportResult implements ImportResult {

    private Long recordNumber;
    private ImportResultStatus coachImportStatus;
    private ImportResultStatus rosterImportStatus;
    private CoachRoster roster;
    private String message;
}
