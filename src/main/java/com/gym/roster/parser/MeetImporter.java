package com.gym.roster.parser;

import com.doubletuck.gym.common.model.Event;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.Meet;
import com.gym.roster.domain.MeetScore;
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
import java.util.ArrayList;
import java.util.List;

public class MeetImporter extends AbstractImporter<MeetImportResult> {

    private final static Logger logger = LoggerFactory.getLogger(MeetImporter.class);

    private final CollegeService collegeService;
    private final AthleteRosterService athleteRosterService;
    private final MeetService meetService;

    private File file;
    private CSVRecord currentRecord;
    private MeetImportResult currentImportResult;
    private List<MeetImportResult> importResults = new ArrayList<>();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    private Meet meet;

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
        this.currentRecord = null;
        this.currentImportResult = null;
        this.importResults.clear();
        this.meet = null;

        try {
            Reader fileReader = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
                    .setHeader(MeetImporter.Headers.class)
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .get()
                    .parse(fileReader);

            for (CSVRecord record : records) {
                currentRecord = record;
                currentImportResult = new MeetImportResult();
                currentImportResult.setFileName(file.getName());
                currentImportResult.setRecordNumber(record.getRecordNumber());

                // If the meet is null, then this is the first record. Retrieve from the database
                // and create it if not found. If the meet is not found or cannot be created, then
                // terminate processing since all scores require a meet instance.
                fetchOrCreateMeet();
                if (meet == null) {
                    logger.error("Meet Import {} - Record {} - Terminate processing since a meet instance cannot be found or created for event name '{}' on date {}",
                            file.getName(),
                            record.getRecordNumber(),
                            record.get(Headers.MEET_NAME),
                            record.get(Headers.DATE));
                    importResults.add(currentImportResult);
                    break;
                }

                AthleteRoster athleteRoster = fetchAthleteRoster(meet.getEventDate());
                if (athleteRoster != null) {
                    fetchOrCreateMeetScore(meet, athleteRoster);
                }
                importResults.add(currentImportResult);
            }
        } catch (IOException e) {
            logger.error("Meet Import {} - An error occurred while parsing the athlete roster CSV file: {}",
                    file.getName(), e.getMessage());
            throw e;
        }
        return importResults;
    }

    private void fetchOrCreateMeet() {
        // If the meet value already exists, then exit. Earlier processing already established
        // this value for other file records to use.
        if (this.meet != null) return;

        // If the meet does not exist, then look it up in the database.
        LocalDate eventDate = LocalDate.parse(currentRecord.get(Headers.DATE), dateFormatter);
        String eventName = currentRecord.get(Headers.MEET_NAME);
        this.meet = meetService.findByDateAndName(eventDate, eventName);

        // If the meet does not exist in the database, then create it.
        if (this.meet == null) {
            Meet newMeet = new Meet();
            newMeet.setEventDate(eventDate);
            newMeet.setEventName(eventName);
            try {
                this.meet = meetService.saveMeet(newMeet);
                logger.info("Meet Import {} - Record {} - Meet created: {}", file.getName(), currentRecord.getRecordNumber(), meet);
            } catch (Exception e) {
                currentImportResult.setImportStatus(ImportResultStatus.ERROR);
                logger.error("Meet Import {} - Record {} - Error creating new Meet: {}. Error: {}", file.getName(), currentRecord.getRecordNumber(), eventName, e.getMessage());
                this.meet = null;
            }
        } else {
            logger.info("Meet Import {} - Record {} - Meet exists: {}", file.getName(), currentRecord.getRecordNumber(), meet);
        }
    }

    private AthleteRoster fetchAthleteRoster(LocalDate eventDate) {

        int seasonYear = eventDate.getYear();
        String collegeCodeName = currentRecord.get(Headers.TEAM);
        String athleteName = currentRecord.get(Headers.ATHLETE_NAME);
        String[] athleteNameArray = parseName(athleteName);

        try {
            return athleteRosterService.findByYearCollegeNameAndAthleteName((short)seasonYear, collegeCodeName, athleteNameArray[0], athleteNameArray[1]);
        } catch (Exception e) {
            logger.error("Meet Import {} - Record {} - No athlete roster found for year = {}, college = '{}', and athlete name '{}': {}",
                    file.getName(), currentRecord.getRecordNumber(), meet.getEventDate().getYear(), collegeCodeName,
                    athleteName, e.getMessage());
            currentImportResult.setImportStatus(ImportResultStatus.ERROR);
            currentImportResult.setMessage("No athlete roster found for year = " + meet.getEventDate().getYear() + ", college = '" + collegeCodeName + "', and athlete name '" + athleteName + "'");
        }

        return null;
    }

    private MeetScore fetchOrCreateMeetScore(Meet meet, AthleteRoster athleteRoster) {

        Event event = Event.find(currentRecord.get(Headers.EVENT));

        MeetScore meetScore = meetService.findScoreByMeetAthleteRosterAndEvent(meet, athleteRoster, event);
        if (meetScore == null) {
            try {
                meetScore = new MeetScore();
                meetScore.setMeet(meet);
                meetScore.setAthleteRoster(athleteRoster);
                meetScore.setEvent(event);
                meetScore.setRotation(parseInteger(currentRecord.get(Headers.ROUND)));
                meetScore.setStartOrder(parseInteger(currentRecord.get(Headers.ORDER)));
                meetScore.setFinalScore(parseDouble(currentRecord.get(Headers.SCORE)));
                meetScore.setDifficultyScore(parseDouble(currentRecord.get(Headers.DIFFICULTY)));
                meetScore.setExecutionScore(parseDouble(currentRecord.get(Headers.EXECUTION)));
                meetScore.setNeutralDeduction(parseDouble(currentRecord.get(Headers.NEUTRAL_DEDUCTION)));
                meetScore.setBonusPoints(parseDouble(currentRecord.get(Headers.STICK_BONUS)));
                meetScore.setHasScoreInquiry(parseBoolean(currentRecord.get(Headers.INQUIRY)));
                meetScore.setIsScoreEdited(parseBoolean(currentRecord.get(Headers.EDITED)));
                meetScore = meetService.saveMeetScore(meetScore);
                currentImportResult.setImportStatus(ImportResultStatus.CREATED);
                logger.info("Meet Import {} - Record {} - MeetScore created: {}", file.getName(), currentRecord.getRecordNumber(), meetScore);
            } catch (Exception e) {
                currentImportResult.setImportStatus(ImportResultStatus.ERROR);
                currentImportResult.setMessage("Error creating new MeetScore: " + e.getMessage());
                logger.error("Meet Import {} - Error creating new MeetScore: {}. Error: {}", file.getName(), currentRecord.getRecordNumber(), e.getMessage());
            }
        } else {
            currentImportResult.setImportStatus(ImportResultStatus.EXISTS);
            logger.info("Meet Import {} - Record {} - MeetScore exists: {}", file.getName(), currentRecord.getRecordNumber(), meetScore);
        }

        currentImportResult.setMeetScore(meetScore);
        return meetScore;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        return Integer.parseInt(value);
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        return Double.parseDouble(value);
    }

    private boolean parseBoolean(String value) {
        if (value == null || value.isBlank()) return false;

        String cleanValue = value.trim().toLowerCase();
        if (cleanValue.equals("y") ||
                cleanValue.equals("true") ||
                cleanValue.equals("t")) return true;
        return false;
    }

    private String[] parseName(String nameText) {
        String[] firstAndLastNameArray = new String[2];

        if (nameText == null || nameText.isBlank()) return firstAndLastNameArray;

        nameText = nameText.trim();
        String[] tempArray = nameText.split("\\s+");
        if (tempArray.length == 1) {
            firstAndLastNameArray[0] = tempArray[0];
        } else if (tempArray.length == 2) {
            firstAndLastNameArray = tempArray;
        } else {
            StringBuilder builder = new StringBuilder();
            for(int i=0; i<tempArray.length-1; i++) {
                if (i > 0) builder.append(" ");
                builder.append(tempArray[i]);
            }
            firstAndLastNameArray[0] = builder.toString();
            firstAndLastNameArray[1] = tempArray[tempArray.length - 1];
        }

        return firstAndLastNameArray;
    }
}
