package com.gym.roster.parser;

import com.gym.roster.domain.College;
import com.gym.roster.service.CollegeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRosterCsvImporter {

    private final static Logger logger = LoggerFactory.getLogger(AbstractRosterCsvImporter.class);
    final Map<String, College> collegeMap = new HashMap<>();
    File file;

    public void parseFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            throw new IllegalArgumentException("File upload stream cannot be null.");
        }
        logger.info("Starting the import of the uploaded data file: {}. Size of file upload: {}.", multipartFile.getOriginalFilename(), multipartFile.getSize());
        File tempFile =  File.createTempFile("roster-", ".tmp");
        multipartFile.transferTo(tempFile);
        logger.info("Saved uploaded data file to temporary file: {}.", tempFile.getAbsoluteFile());

        parseFile(tempFile);
        tempFile.deleteOnExit();
    }

    public void parseFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        this.file = file;
        parseFile();
    }

    abstract void parseFile() throws IOException;
    abstract CollegeService getCollegeService();

    College fetchCollege(String collegeCodeName) {
        if (collegeCodeName == null || collegeCodeName.isEmpty()) {
            return null;
        }

        College college = collegeMap.get(collegeCodeName);
        if (college == null) {
            College tempCollege = getCollegeService().findByCodeName(collegeCodeName);
            if (tempCollege != null) {
                collegeMap.put(collegeCodeName, tempCollege);
                college = tempCollege;
            }
        }
        return college;
    }
}
