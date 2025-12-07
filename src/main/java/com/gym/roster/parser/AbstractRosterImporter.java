package com.gym.roster.parser;

import com.gym.roster.domain.College;
import com.gym.roster.service.CollegeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractRosterImporter<T extends ImportResult> {

    private final static Logger logger = LoggerFactory.getLogger(AbstractRosterImporter.class);
    final Map<String, College> collegeMap = new HashMap<>();
    
    long importFilesTotalCount = 0;
    long importFilesProcessedCount = 0;
    long importFilesSkippedCount = 0;
    long importRecordsTotalCount = 0;
    long importRecordsErrorCount = 0;

    public void parseDirectory(String directoryPath) throws IOException {
        if (directoryPath == null || directoryPath.isEmpty()) {
            throw new IllegalArgumentException("Directory path cannot be null or empty.");
        }


        Path path = Paths.get(directoryPath);
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Directory path is not a valid directory: " + directoryPath);
        }

        logger.info("Directory Import {} - Starting", directoryPath);
        resetCounts();

        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        importFilesTotalCount++;
                        try {
                            if (isValidRosterFileType(filePath.getFileName().toString())) {
                                importFilesProcessedCount++;
                                List<T> results = parseFile(filePath.toFile());
                                appendCounts(results);
                            } else {
                                importFilesSkippedCount++;
                                logger.debug(
                                        "Skip import for '{}' - Not a valid file type for the roster import.",
                                        filePath.getFileName());
                            }
                        } catch (IOException e) {
                            logger.error("Error parsing roster file: " + filePath.getFileName(), e);
                        }
                    });
        }

        logger.info("Directory Import {} - Total files in directory: {}.", directoryPath, importFilesTotalCount);
        logger.info("Directory Import {} - Total files processed: {}.", directoryPath, importFilesProcessedCount);
        logger.info("Directory Import {} - Total files skipped: {}.", directoryPath, importFilesSkippedCount);
        logger.info("Directory Import {} - Total records processed: {}.", directoryPath, importRecordsTotalCount);
        logger.info("Directory Import {} - Total records with errors: {}.", directoryPath, importRecordsErrorCount);
    }

    public List<T> parseFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            throw new IllegalArgumentException("File upload stream cannot be null.");
        }
        logger.info("Starting the import of the uploaded data file: {}. Size of file upload: {}.",
                multipartFile.getOriginalFilename(), multipartFile.getSize());
        File tempFile = File.createTempFile("roster-", ".tmp");
        multipartFile.transferTo(tempFile);
        logger.info("Saved uploaded data file to temporary file: {}.", tempFile.getAbsoluteFile());

        List<T> results = parseFile(tempFile);
        tempFile.deleteOnExit();
        return results;
    }

    abstract boolean isValidRosterFileType(String fileName);

    abstract List<T> parseFile(File file) throws IOException;

    abstract CollegeService getCollegeService();

    /**
     * Given a college code, fetch the corresponding college object. If the college
     * code is empty or null, or if no corresponding college is found, then a null
     * value is returned.
     * 
     * @param collegeCodeName The code name of the college to fetch.
     * @return The college object corresponding to the code name. Or, null if no
     * matching college is found.
     */
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
    
    private void appendCounts(List<T> importResults) {

        importRecordsTotalCount += importResults.size();
        for (T result : importResults) {
            if (result.hasErrorStatus()) {
                importRecordsErrorCount++;
            }
        }
    }

    private void resetCounts() {
        importFilesSkippedCount = 0;
        importFilesTotalCount = 0;
        importRecordsErrorCount = 0;
        importRecordsTotalCount = 0;
    }
}
