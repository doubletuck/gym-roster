package com.gym.roster.parser;

import com.gym.roster.domain.College;
import com.gym.roster.domain.Conference;
import com.gym.roster.domain.Division;
import com.gym.roster.domain.Region;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CollegeCsvImporter {

    private final static Logger logger = LoggerFactory.getLogger(CollegeCsvImporter.class);

    public enum Headers {
        CODE_NAME, SHORT_NAME, LONG_NAME, CITY, STATE,
        CONFERENCE, DIVISION, REGION,
        NICKNAME, TEAM_URL
    }

    public static List<College> parseFile(MultipartFile multipartFile) throws IOException{
        if (multipartFile == null) {
            throw new IllegalArgumentException("File upload stream cannot be null.");
        }
        File tempFile =  File.createTempFile("upload", ".tmp");
        multipartFile.transferTo(tempFile);
        logger.debug("Saved uploaded college data file to a temporary file: {}.", tempFile.getAbsoluteFile());

        List<College> parsedFile = CollegeCsvImporter.parseFile(tempFile);
        tempFile.deleteOnExit();
        return parsedFile;
    }

    public static List<College> parseFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        ArrayList<College> colleges = new ArrayList<>();
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
                College college = new College();
                college.setCodeName(record.get(Headers.CODE_NAME));
                college.setLongName(record.get(Headers.LONG_NAME));
                college.setShortName(record.get(Headers.SHORT_NAME));
                college.setNickname(record.get(Headers.NICKNAME));
                college.setCity(record.get(Headers.CITY));
                college.setState(record.get(Headers.STATE));
                college.setRegion(Region.find(record.get(Headers.REGION)));
                college.setDivision(Division.find(record.get(Headers.DIVISION)));
                college.setConference(Conference.find(record.get(Headers.CONFERENCE)));
                college.setTeamUrl(record.get(Headers.TEAM_URL));
                colleges.add(college);
                logger.debug("File record {} - raw values: {}", record.getRecordNumber(), record.values());
                logger.debug("File record {} - parsed values: {}", record.getRecordNumber(), college);
            }
        } catch (FileNotFoundException e) {
            logger.error("Cannot parse college CSV data because file '{}' could not be found.", file.getAbsolutePath(), e);
            throw e;
        } catch (IOException e) {
            logger.error("An error occurred while parsing the college CSV file '{}'.", file.getAbsoluteFile(), e);
            throw e;
        }

        return colleges;
    }
}
