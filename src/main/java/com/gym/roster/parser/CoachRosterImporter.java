package com.gym.roster.parser;

import com.doubletuck.gym.common.model.StaffRole;
import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.service.CoachRosterService;
import com.gym.roster.service.CoachService;
import com.gym.roster.service.CollegeService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CoachRosterImporter extends AbstractRosterImporter<CoachRosterImportResult> {

    private final static Logger logger = LoggerFactory.getLogger(CoachRosterImporter.class);

    private final CollegeService collegeService;
    private final CoachService coachService;
    private final CoachRosterService coachRosterService;

    private File file;
    private CoachRosterImportResult currentImportResult;
    private final List<CoachRosterImportResult> importResults = new ArrayList<>();

    public CoachRosterImporter(CollegeService collegeService, CoachService coachService, CoachRosterService coachRosterService) {
        this.collegeService = collegeService;
        this.coachService = coachService;
        this.coachRosterService = coachRosterService;
    }

    public enum Headers {
        COLLEGE_CODE_NAME, YEAR,
        FIRST_NAME, LAST_NAME, ROLE
    }

    CollegeService getCollegeService() {
        return collegeService;
    }

    boolean isValidRosterFileType(String fileName) {
        boolean containsAthleteRoster = fileName != null && fileName.toLowerCase().contains("staff-roster");
        boolean isCsv = fileName != null && fileName.toLowerCase().endsWith(".csv");
        return containsAthleteRoster && isCsv;
    }

    List<CoachRosterImportResult> parseFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        this.file = file;
        this.currentImportResult = null;
        this.importResults.clear();

        try {
            Reader fileReader = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
                    .setHeader(Headers.class)
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .get()
                    .parse(fileReader);

            for (CSVRecord record : records) {
                logger.info("CoachRoster Import {} - Record {} - Processing file record: {}", file.getName(), record.getRecordNumber(), record);
                currentImportResult = new CoachRosterImportResult();
                currentImportResult.setRecordNumber(record.getRecordNumber());

                String collegeCodeName = record.get(Headers.COLLEGE_CODE_NAME);
                College college = fetchCollege(collegeCodeName);
                if (college == null) {
                    currentImportResult.setRosterImportStatus(ImportResultStatus.ERROR);
                    currentImportResult.setMessage(String.format("College name given in the file is not valid or is not supported: %s.", collegeCodeName));
                    importResults.add(currentImportResult);
                    continue;
                }

                Short seasonYear = Short.parseShort(record.get(Headers.YEAR));
                StaffRole role = StaffRole.valueOf(record.get(Headers.ROLE));
                Coach coach = fetchCoach(record);
                if (coach == null) {
                    currentImportResult.setRosterImportStatus(ImportResultStatus.ERROR);
                    currentImportResult.setMessage(String.format("Coach given in the file is not valid: %s %s.",
                            record.get(Headers.FIRST_NAME), record.get(Headers.LAST_NAME)));
                    continue;
                }

                CoachRoster roster = coachRosterService.findByYearAndCollegeAndCoach(seasonYear, college, coach);
                if (roster == null) {
                    roster = new CoachRoster();
                    roster.setCollege(college);
                    roster.setSeasonYear(seasonYear);
                    roster.setCoach(coach);
                    roster.setRoleCode(role);
                    try {
                        roster = coachRosterService.save(roster);
                        currentImportResult.setRosterImportStatus(ImportResultStatus.CREATED);
                        logger.info("CoachRoster Import {} - Record {} - CoachRoster created: {}", file.getName(), record.getRecordNumber(), roster);
                    } catch (Exception e) {
                        currentImportResult.setRosterImportStatus(ImportResultStatus.ERROR);
                        currentImportResult.setMessage("Error creating CoachRoster: " + e.getMessage());
                        logger.error("CoachRoster Import {} - Record {} - CoachRoster creation failed: {}",
                                file.getName(), record.getRecordNumber(), roster, e.getMessage());
                        continue;
                    }
                } else {
                    currentImportResult.setRosterImportStatus(ImportResultStatus.EXISTS);
                    logger.info("CoachRoster Import {} - Record {} - CoachRoster exists: {}", file.getName(), record.getRecordNumber(), roster);
                }

                currentImportResult.setRoster(roster);
                importResults.add(currentImportResult);
            }
            logger.info("CoachRoster Import {} - File processing completed.", file.getName());
        } catch (IOException e) {
            logger.error("An error occurred while parsing the coach roster CSV file '{}': {}", file.getAbsoluteFile(),
                    e.getMessage());
            throw e;
        }
        return this.importResults;
    }

    private Coach fetchCoach(CSVRecord record) {

        String firstName = record.get(Headers.FIRST_NAME);
        String lastName = record.get(Headers.LAST_NAME);

        Coach coach = coachService.findByName(firstName, lastName);

        if (coach == null) {
            coach = new Coach();
            coach.setFirstName(firstName);
            coach.setLastName(lastName);
            try {
                coach = coachService.save(coach);
                currentImportResult.setCoachImportStatus(ImportResultStatus.CREATED);
                logger.info("CoachRoster Import {} - Record {} - Coach created: {}.", file.getName(), record.getRecordNumber(), coach);
                logger.info("CoachRoster Import {} - Record {} - Created new Coach: {}.", file.getName(), record.getRecordNumber(), coach);
            } catch (Exception e) {
                currentImportResult.setCoachImportStatus(ImportResultStatus.ERROR);
                currentImportResult.setMessage("Error creating new Coach: " + e.getMessage());
                logger.error("CoachRoster Import {} - Record {} - Error creating new Coach: {}. Error: {}", file.getName(),
                        record.getRecordNumber(), coach, e.getMessage());
                return null;
            }
        } else {
            currentImportResult.setCoachImportStatus(ImportResultStatus.EXISTS);
            logger.info("CoachRoster Import {} - Record {} - Coach exists: {}.", file.getName(), record.getRecordNumber(), coach);
        }

        return coach;
    }
}
