package com.gym.roster.parser;

import com.gym.roster.domain.College;
import lombok.Data;

@Data
public class CollegeImportResult implements ImportResult {

    private String fileName;
    private Long recordNumber;
    private String collegeCodeName;
    private ImportResultStatus importStatus = ImportResultStatus.UNPROCESSED;
    private College college;
    private String message;

    public boolean hasErrorStatus() {
        return importStatus == ImportResultStatus.ERROR;
    }
}
