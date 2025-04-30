package com.gym.roster.parser;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.College;
import com.gym.roster.domain.Roster;
import com.gym.roster.service.AthleteService;
import com.gym.roster.service.CollegeService;
import com.gym.roster.service.RosterService;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AthleteRosterCsvImporter {

    private final static Logger logger = LoggerFactory.getLogger(AthleteRosterCsvImporter.class);

    private final CollegeService collegeService;
    private final AthleteService athleteService;
    private final RosterService rosterService;

    @Getter
    private final List<AthleteRosterImportResult> importResults = new ArrayList<>();
    private final Map<String, College> collegeMap = new HashMap<>();
    private File file;
    private AthleteRosterImportResult currentImportResult;

    public AthleteRosterCsvImporter(CollegeService collegeService, AthleteService athleteService, RosterService rosterService) {
        this.collegeService = collegeService;
        this.athleteService = athleteService;
        this.rosterService = rosterService;
    }

    public enum Headers {
        COLLEGE_CODE_NAME, YEAR,
        FIRST_NAME, LAST_NAME, COLLEGE_CLASS,
        HOME_TOWN, HOME_STATE, HOME_COUNTRY,
        CLUB, POSITION
    }

    public void parseFile(MultipartFile multipartFile) throws IOException{
        if (multipartFile == null) {
            throw new IllegalArgumentException("File upload stream cannot be null.");
        }
        logger.info("Saving uploaded data file '{}' to a temporary file. Size of file upload: {}.", multipartFile.getOriginalFilename(), multipartFile.getSize());
        File tempFile =  File.createTempFile("upload", ".tmp");
        multipartFile.transferTo(tempFile);
        logger.info("Saved uploaded data file to temporary file: {}.", tempFile.getAbsoluteFile());

        parseFile(tempFile);
        logger.info("Roster Import {} - File processing completed. Deleting temporary file on exit.", tempFile.getName());
        tempFile.deleteOnExit();
    }

    public void parseFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }

        this.file = file;
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
                logger.info("Roster Import {} - Record {} - Processing file record: {}", file.getName(), record.getRecordNumber(), record);
                currentImportResult = new AthleteRosterImportResult();
                currentImportResult.setRecordNumber(record.getRecordNumber());

                Short seasonYear = Short.parseShort(record.get(Headers.YEAR));
                College college = fetchCollege(record.get(Headers.COLLEGE_CODE_NAME));
                Athlete athlete = fetchAthlete(record);
                Roster roster = rosterService.findByYearCollegeAndAthlete(seasonYear, college, athlete);
                String academicClassCode = record.get(Headers.COLLEGE_CLASS).isBlank() ? null : record.get(Headers.COLLEGE_CLASS);
                String position = record.get(Headers.POSITION).isBlank() ? null : record.get(Headers.POSITION);

                if (roster != null) {
                    currentImportResult.setRosterImportStatus(AthleteRosterImportResult.Status.EXISTS);
                    logger.info("Roster Import {} - Record {} - Roster exists: {}", file.getName(), record.getRecordNumber(), roster);
                } else {
                    roster = new Roster();
                    roster.setCollege(college);
                    roster.setSeasonYear(seasonYear);
                    roster.setAthlete(athlete);
                    roster.setClassCode(academicClassCode);
                    roster.setPosition(position);
                    rosterService.save(roster);
                    currentImportResult.setRosterImportStatus(AthleteRosterImportResult.Status.CREATED);
                    logger.info("Roster Import {} - Record {} - Roster created: {}", file.getName(), record.getRecordNumber(), roster);
                }

                currentImportResult.setRoster(roster);
                importResults.add(currentImportResult);
            }
        } catch (IOException e) {
            logger.error("An error occurred while parsing the athlete roster CSV file '{}'.", file.getAbsoluteFile(), e);
            throw e;
        }
    }

    private Athlete fetchAthlete(CSVRecord record) {

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
            athlete = athleteService.save(athlete);
            currentImportResult.setAthleteImportStatus(AthleteRosterImportResult.Status.CREATED);
            logger.info("Roster Import {} - Record {} - Athlete created: {}.", file.getName(), record.getRecordNumber(), athlete);
        } else {
            if (athlete.getClubName() == null && clubName != null) {
                athlete.setClubName(clubName);
                athleteService.save(athlete);
                currentImportResult.setAthleteImportStatus(AthleteRosterImportResult.Status.UPDATED);
                logger.info("Roster Import {} - Record {} - Athlete updated: {}.", file.getName(), record.getRecordNumber(), athlete);
            } else {
                currentImportResult.setAthleteImportStatus(AthleteRosterImportResult.Status.EXISTS);
                logger.info("Roster Import {} - Record {} - Athlete exists: {}.", file.getName(), record.getRecordNumber(), athlete);
            }
        }

        return athlete;
    }

    private College fetchCollege(String collegeCodeName) {
        if (collegeCodeName == null || collegeCodeName.isEmpty()) {
            return null;
        }

        College college = collegeMap.get(collegeCodeName);
        if (college == null) {
            College tempCollege = collegeService.findByCodeName(collegeCodeName);
            if (tempCollege != null) {
                collegeMap.put(collegeCodeName, tempCollege);
                college = tempCollege;
            }
        }
        return college;
    }
}
