package com.gym.roster.parser;

public interface ImportResult {
    public String getFileName();
    public Long getRecordNumber();
    public String getMessage();
    public boolean hasErrorStatus();
}
