package com.gym.roster.parser;

import com.doubletuck.gym.common.model.StaffRole;
import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.service.CoachRosterService;
import com.gym.roster.service.CoachService;
import com.gym.roster.service.CollegeService;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CoachRosterImporter extends AbstractRosterImporter {

    private final static Logger logger = LoggerFactory.getLogger(CoachRosterImporter.class);

    private final CollegeService collegeService;
    private final CoachService coachService;
    private final CoachRosterService coachRosterService;

    @Getter
    private final List<CoachRosterImportResult> importResults = new ArrayList<>();
    private CoachRosterImportResult currentImportResult;

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

    void parseFile() throws IOException {
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
                    currentImportResult.setRosterImportStatus(CoachRosterImportResult.Status.ERROR);
                    currentImportResult.setMessage("College name given in the file is not valid or is not supported: " + collegeCodeName + ".");
                    continue;
                }

                Short seasonYear = Short.parseShort(record.get(Headers.YEAR));
                Coach coach = fetchCoach(record);
                StaffRole role = StaffRole.valueOf(record.get(Headers.ROLE));

                CoachRoster roster = coachRosterService.findByYearAndCollegeAndCoach(seasonYear, college, coach);
                if (roster == null) {
                    roster = new CoachRoster();
                    roster.setCollege(college);
                    roster.setSeasonYear(seasonYear);
                    roster.setCoach(coach);
                    roster.setRoleCode(role);
                    roster = coachRosterService.save(roster);
                    currentImportResult.setRosterImportStatus(CoachRosterImportResult.Status.CREATED);
                    logger.info("CoachRoster Import {} - Record {} - CoachRoster created: {}", file.getName(), record.getRecordNumber(), roster);
                } else {
                    currentImportResult.setRosterImportStatus(CoachRosterImportResult.Status.EXISTS);
                    logger.info("CoachRoster Import {} - Record {} - CoachRoster exists: {}", file.getName(), record.getRecordNumber(), roster);
                }

                currentImportResult.setRoster(roster);
                importResults.add(currentImportResult);
            }
            logger.info("CoachRoster Import {} - File processing completed.", file.getName());
        } catch (IOException e) {
            logger.error("An error occurred while parsing the coach roster CSV file '{}': {}", file.getAbsoluteFile(), e.getMessage() );
            throw e;
        }
    }

    private Coach fetchCoach(CSVRecord record) {

        String firstName = record.get(Headers.FIRST_NAME);
        String lastName = record.get(Headers.LAST_NAME);

        Coach coach = coachService.findByName(firstName, lastName);

        if (coach == null) {
            coach = new Coach();
            coach.setFirstName(firstName);
            coach.setLastName(lastName);
            coach = coachService.save(coach);
            currentImportResult.setCoachImportStatus(CoachRosterImportResult.Status.CREATED);
            logger.info("CoachRoster Import {} - Record {} - Coach created: {}.", file.getName(), record.getRecordNumber(), coach);
        } else {
            currentImportResult.setCoachImportStatus(CoachRosterImportResult.Status.EXISTS);
            logger.info("CoachRoster Import {} - Record {} - Coach exists: {}.", file.getName(), record.getRecordNumber(), coach);
        }

        return coach;
    }
}
