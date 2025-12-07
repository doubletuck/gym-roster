package com.gym.roster.parser;

import com.doubletuck.gym.common.model.AcademicYear;
import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.College;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.service.AthleteService;
import com.gym.roster.service.CollegeService;
import com.gym.roster.service.AthleteRosterService;
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

public class AthleteRosterImporter extends AbstractRosterImporter<AthleteRosterImportResult> {

    private final static Logger logger = LoggerFactory.getLogger(AthleteRosterImporter.class);

    private final CollegeService collegeService;
    private final AthleteService athleteService;
    private final AthleteRosterService athleteRosterService;

    private File file;
    private AthleteRosterImportResult currentImportResult;
    private final List<AthleteRosterImportResult> importResults = new ArrayList<>();

    public AthleteRosterImporter(CollegeService collegeService, AthleteService athleteService,
            AthleteRosterService athleteRosterService) {
        this.collegeService = collegeService;
        this.athleteService = athleteService;
        this.athleteRosterService = athleteRosterService;
    }

    public enum Headers {
        COLLEGE_CODE_NAME, YEAR,
        FIRST_NAME, LAST_NAME, ACADEMIC_YEAR,
        HOME_TOWN, HOME_STATE, HOME_COUNTRY,
        CLUB, EVENT
    }

    CollegeService getCollegeService() {
        return collegeService;
    }

    boolean isValidRosterFileType(String fileName) {
        boolean containsAthleteRoster = fileName != null && fileName.toLowerCase().contains("athlete-roster");
        boolean isCsv = fileName != null && fileName.toLowerCase().endsWith(".csv");
        return containsAthleteRoster && isCsv;
    }

    List<AthleteRosterImportResult> parseFile(File file) throws IOException {
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
                logger.info("AthleteRoster Import {} - Record {} - Processing file record: {}", file.getName(),
                        record.getRecordNumber(), record);
                currentImportResult = new AthleteRosterImportResult();
                currentImportResult.setRecordNumber(record.getRecordNumber());

                String collegeCodeName = record.get(CoachRosterImporter.Headers.COLLEGE_CODE_NAME);
                College college = fetchCollege(collegeCodeName);
                if (college == null) {
                    logger.error(
                            "AthleteRoster Import {} - Record {} - The college code name in this record is not valid or is not supported: {}",
                            file.getName(), record.getRecordNumber(), collegeCodeName);
                    currentImportResult.setRosterImportStatus(ImportResultStatus.ERROR);
                    currentImportResult.setMessage(String.format(
                            "College name given in the file is not valid or is not supported: %s.",
                            collegeCodeName));
                } else {
                    Athlete athlete = fetchOrCreateAthlete(record);
                    if (athlete != null) {
                        AthleteRoster roster = fetchOrCreateAthleteRoster(record, college, athlete);
                        currentImportResult.setRoster(roster);
                    }
                }
                importResults.add(currentImportResult);
            }
            logger.info("AthleteRoster Import {} - File processing completed.", file.getName());
        } catch (IOException e) {
            logger.error("An error occurred while parsing the athlete roster CSV file '{}': {}", file.getAbsoluteFile(),
                    e.getMessage());
            throw e;
        }
        return this.importResults;
    }

    private Athlete fetchOrCreateAthlete(CSVRecord record) {

        String firstName = record.get(Headers.FIRST_NAME);
        String lastName = record.get(Headers.LAST_NAME);
        String homeCity = record.get(Headers.HOME_TOWN);
        String homeState = record.get(Headers.HOME_STATE).isBlank() ? null : record.get(Headers.HOME_STATE);
        String homeCountry = record.get(Headers.HOME_COUNTRY).isBlank() ? null : record.get(Headers.HOME_COUNTRY);
        String clubName = record.get(Headers.CLUB).isBlank() ? null : record.get(Headers.CLUB);

        Athlete athlete = athleteService.findByNameAndHomeCity(firstName, lastName, homeCity);

        if (athlete == null) {
            athlete = new Athlete();
            athlete.setFirstName(firstName);
            athlete.setLastName(lastName);
            athlete.setHomeCity(homeCity);
            athlete.setHomeState(homeState);
            athlete.setHomeCountry(homeCountry);
            athlete.setClubName(clubName);
            try {
                athlete = athleteService.save(athlete);
                currentImportResult.setAthleteImportStatus(ImportResultStatus.CREATED);
                logger.info("AthleteRoster Import {} - Record {} - Athlete created: {}.", file.getName(),
                        record.getRecordNumber(), athlete);
            } catch (Exception e) {
                currentImportResult.setAthleteImportStatus(ImportResultStatus.ERROR);
                currentImportResult.setMessage("Error creating Athlete: " + e.getMessage());
                logger.error("AthleteRoster Import {} - Record {} - Athlete creation failed: {}",
                        file.getName(), record.getRecordNumber(), athlete, e.getMessage());
                return null;
            }
        } else {
            if (athlete.getClubName() == null && clubName != null) {
                athlete.setClubName(clubName);
                athleteService.save(athlete);
                currentImportResult.setAthleteImportStatus(ImportResultStatus.UPDATED);
                logger.info("AthleteRoster Import {} - Record {} - Athlete updated: {}.", file.getName(),
                        record.getRecordNumber(), athlete);
            } else {
                currentImportResult.setAthleteImportStatus(ImportResultStatus.EXISTS);
                logger.info("AthleteRoster Import {} - Record {} - Athlete exists: {}.", file.getName(),
                        record.getRecordNumber(), athlete);
            }
        }

        return athlete;
    }

    private AthleteRoster fetchOrCreateAthleteRoster(CSVRecord record, College college, Athlete athlete) {

        Short seasonYear = Short.parseShort(record.get(Headers.YEAR));
        AcademicYear academicYear = AcademicYear.find(record.get(Headers.ACADEMIC_YEAR));
        String event = record.get(Headers.EVENT).isBlank() ? null : record.get(Headers.EVENT);

        AthleteRoster roster = athleteRosterService.findByYearCollegeAndAthlete(seasonYear, college, athlete);
        if (roster == null) {
            roster = new AthleteRoster();
            roster.setCollege(college);
            roster.setSeasonYear(seasonYear);
            roster.setAthlete(athlete);
            roster.setAcademicYear(academicYear);
            roster.setEvents(event);
            try {
                roster = athleteRosterService.save(roster);
                currentImportResult.setRosterImportStatus(ImportResultStatus.CREATED);
                logger.info("AthleteRoster Import {} - Record {} - AthleteRoster created: {}", file.getName(),
                        record.getRecordNumber(), roster);
            } catch (Exception e) {
                currentImportResult.setRosterImportStatus(ImportResultStatus.ERROR);
                currentImportResult.setMessage("Error saving AthleteRoster: " + e.getMessage());
                logger.error("AthleteRoster Import {} - Record {} - AthleteRoster creation failed: {} - Error: {}",
                                file.getName(), record.getRecordNumber(), athlete, e.getMessage());
                importResults.add(currentImportResult);
            }
        } else {
            currentImportResult.setRosterImportStatus(ImportResultStatus.EXISTS);
            logger.info("AthleteRoster Import {} - Record {} - AthleteRoster exists: {}", file.getName(),
                    record.getRecordNumber(), roster);
        }

        return roster;
    }
}
