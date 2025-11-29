package com.gym.roster.service;

import com.gym.roster.domain.College;
import com.gym.roster.exception.ValidationException;
import com.gym.roster.parser.CollegeImporter;
import com.gym.roster.parser.CollegeImportResult;
import com.gym.roster.repository.CollegeRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CollegeService {

    public static final Logger logger = LoggerFactory.getLogger(CollegeService.class);
    private final CollegeRepository collegeRepository;
    private final Validator validator;

    @Autowired
    public CollegeService(CollegeRepository collegeRepository, Validator validator) {
        this.collegeRepository = collegeRepository;
        this.validator = validator;
    }

    public Optional<College> findById(UUID id) {
        return collegeRepository.findById(id);
    }

    public College findByCodeName(String codeName) {
        return collegeRepository.findByCodeName(codeName);
    }

    /**
     * Saves the given college instance to the database. If any fields
     * fail validation, then a ValidationException is thrown. The returned
     * college object will also contain fields that are updated by the
     * system.
     *
     * @param college The college instance that will be saved to the
     * database.
     * @return The saved college instance with any updated fields.
     * @throws ValidationException If the college object has validation
     * constraint errors.
     */
    public College save(College college) {

        Set<ConstraintViolation<College>> errors = validator.validate(college);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Instant now = Instant.now();
        if (college.getId() == null) {
            college.setCreationTimestamp(now);
        }
        college.setLastUpdateTimestamp(now);
        return collegeRepository.save(college);
    }

    public void deleteById(UUID id) {
        collegeRepository.deleteById(id);
    }

    public Page<College> getPaginatedEntities(Pageable pageable) {
        return collegeRepository.findAll(pageable);
    }

    public List<CollegeImportResult> importCollegesFromFile(MultipartFile multipartFile) throws Exception {

        logger.info("Initiating the import of college data from an uploaded file.");
        List<College> fileColleges = CollegeImporter.parseFile(multipartFile);
        logger.info("College data was parsed from the uploaded file. Total colleges parsed: {}.", fileColleges.size());

        ArrayList<CollegeImportResult> collegeImportResults = new ArrayList<>();
        for (College college : fileColleges) {
            collegeImportResults.add(processFileRow(college));
        }

        logger.info("The import of college data from an uploaded file is completed.");
        return collegeImportResults;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected CollegeImportResult processFileRow(College fileCollege) {
        CollegeImportResult importResult = new CollegeImportResult();
        importResult.setCollegeCodeName(fileCollege.getCodeName());

        College dbCollege = findByCodeName(fileCollege.getCodeName());
        if (dbCollege == null) {
            try {
                dbCollege = save(fileCollege);
                importResult.setImportStatus(CollegeImportResult.Status.CREATED);
            } catch (ValidationException e) {
                importResult.setImportStatus(CollegeImportResult.Status.ERROR);
                importResult.setMessage("Saving failed because of the following validation errors: "+ e.getErrorListText());
                logger.error("Validation errors occurred when trying to save this college imported from the file: {}. The validation errors are: {}", fileCollege, e.getErrorListText());
            } catch (Exception e) {
                importResult.setImportStatus(CollegeImportResult.Status.ERROR);
                importResult.setMessage(e.getMessage());
                logger.error(e.getMessage(), e);
            }
        } else {
            importResult.setImportStatus(CollegeImportResult.Status.EXISTS);
        }
        importResult.setCollege(dbCollege);
        logger.info("Import data result: {}", importResult);
        return importResult;
    }
}
