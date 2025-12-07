package com.gym.roster.parser;

import com.gym.roster.domain.CoachRoster;
import lombok.Data;

@Data
public class CoachRosterImportResult implements ImportResult {

    private Long recordNumber;
    private ImportResultStatus coachImportStatus = ImportResultStatus.UNPROCESSED;
    private ImportResultStatus rosterImportStatus = ImportResultStatus.UNPROCESSED;
    private CoachRoster roster;
    private String message;

    public boolean hasErrorStatus() {
        return (coachImportStatus == ImportResultStatus.ERROR ||
                rosterImportStatus == ImportResultStatus.ERROR);
    }
}
