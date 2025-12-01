package com.gym.roster.parser;

import com.gym.roster.domain.College;
import lombok.Data;

@Data
public class CollegeImportResult implements ImportResult {

    private String collegeCodeName;
    private ImportResultStatus importStatus;
    private College college;
    private String message;

    public boolean hasErrorStatus() {
        return importStatus == ImportResultStatus.ERROR;
    }
}
