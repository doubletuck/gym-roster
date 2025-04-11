package com.gym.roster.parser;

import com.gym.roster.domain.College;
import lombok.Data;

@Data
public class CollegeImportResult {

    public enum Status {
        CREATED,
        EXISTS,
        ERROR;
    }

    private String collegeShortName;
    private Status importStatus;
    private College college;
}
