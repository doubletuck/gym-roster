package com.gym.roster.parser;

import com.gym.roster.domain.MeetScore;
import lombok.Data;

@Data
public class MeetImportResult implements ImportResult {

    private String fileName;
    private Long recordNumber;
    private MeetScore meetScore;
    private ImportResultStatus importStatus = ImportResultStatus.UNPROCESSED;
    private String message;

    public boolean hasErrorStatus() {
        return importStatus == ImportResultStatus.ERROR;
    }
}
