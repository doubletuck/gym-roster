package com.gym.roster.parser;

import com.gym.roster.domain.College;
import lombok.Data;

@Data
public class CollegeImportResult {

    public enum Status {
        CREATED,
        UPDATED,
        EXISTS,
        ERROR;
    }

    private String collegeCodeName;
    private Status importStatus;
    private College college;
    private String message;
}
