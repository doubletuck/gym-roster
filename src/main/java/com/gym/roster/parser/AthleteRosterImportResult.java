package com.gym.roster.parser;

import com.gym.roster.domain.AthleteRoster;
import lombok.Data;

@Data
public class AthleteRosterImportResult implements ImportResult {

    private Long recordNumber;
    private ImportResultStatus athleteImportStatus = ImportResultStatus.UNPROCESSED;
    private ImportResultStatus rosterImportStatus = ImportResultStatus.UNPROCESSED;
    private AthleteRoster roster;
    private String message;

    public boolean hasErrorStatus() {
        return (athleteImportStatus == ImportResultStatus.ERROR ||
                rosterImportStatus == ImportResultStatus.ERROR);
    }
}
