package com.gym.roster.parser;

import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.Meet;
import com.gym.roster.service.AthleteRosterService;
import com.gym.roster.service.CollegeService;
import com.gym.roster.service.MeetService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class MeetImporter extends AbstractImporter<MeetImportResult> {

    private final static Logger logger = LoggerFactory.getLogger(MeetImporter.class);

    private final CollegeService collegeService;
    private final AthleteRosterService athleteRosterService;
    private final MeetService meetService;

    private File file;
    private MeetImportResult currentImportResult;
    private List<MeetImportResult> importResults = new ArrayList<>();

    public MeetImporter(CollegeService collegeService,
                        AthleteRosterService athleteRosterService,
                        MeetService meetService) {
        this.collegeService = collegeService;
        this.athleteRosterService = athleteRosterService;
        this.meetService = meetService;
    }

    public enum Headers {
        ROUND, ORDER, ATHLETE_NAME,
        TEAM, EVENT, SCORE,
        DIFFICULTY, EXECUTION,
        NEUTRAL_DEDUCTION,
        STICK_BONUS, INQUIRY, EDITED,
        DATE, MEET_NAME
    }

    CollegeService getCollegeService() {
        return collegeService;
    }

    boolean isValidRosterFileType(String fileName) {
        return fileName != null && fileName.toLowerCase().endsWith(".csv");
    }

    List<MeetImportResult> parseFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        this.file = file;
        this.currentImportResult = null;
        this.importResults.clear();

        try {
            Reader fileReader = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
                    .setHeader(MeetImporter.Headers.class)
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .get()
                    .parse(fileReader);

            // TODO: have each record processed individually in a separate method.

            for (CSVRecord record : records) {
                currentImportResult = new MeetImportResult();
                currentImportResult.setFileName(file.getName());
                currentImportResult.setRecordNumber(record.getRecordNumber());
                importResults.add(currentImportResult);

                // STEP: Find meet. If it doesn't exist, then create it
                // STEP: Find athlete roster. If it doesn't exist, then record as an error.
                String collegeCodeName = record.get(Headers.TEAM);
                String athleteName = record.get(Headers.ATHLETE_NAME);
                String meetDateString = record.get(Headers.DATE);
                AthleteRoster athleteRoster = fetchAthleteRoster(collegeCodeName, athleteName, meetDateString);
                if (athleteRoster != null) {
                }


                String meetName = record.get(Headers.MEET_NAME);
                String score = record.get(Headers.SCORE);
                String event = record.get(Headers.EVENT);
                String difficulty = record.get(Headers.DIFFICULTY);
                String execution = record.get(Headers.EXECUTION);
            }

        } catch (IOException e) {
            logger.error("An error occurred while parsing the athlete roster CSV file '{}': {}", file.getAbsoluteFile(),
                    e.getMessage());
            throw e;
        }
        return importResults;
    }

    private Meet fetchOrCreateMeet() {
        return null;
    }

    private AthleteRoster fetchAthleteRoster(String collegeCodeName, String athleteName, String meetDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try {
            LocalDate meetDate = LocalDate.parse(meetDateString, formatter);
            // Get the year from meet date
            // call athlete roster service and look for athlete by college, name and season.
            // return roster or null if nothing found.
            // if null or error, then write result message
        } catch( DateTimeParseException e) {
            // do something, write to result
        }

        return null;
    }


}
