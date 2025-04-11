package com.gym.roster.service;

import com.gym.roster.domain.College;
import com.gym.roster.parser.CollegeCsvImporter;
import com.gym.roster.parser.CollegeImportResult;
import com.gym.roster.repository.CollegeRepository;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollegeService {

    public static final Logger logger = LoggerFactory.getLogger(CollegeService.class);
    private final CollegeRepository collegeRepository;

    @Autowired
    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    public Optional<College> findById(UUID id) {
        return collegeRepository.findById(id);
    }

    public College save(College college) {
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

    public List<College> findAll() {
        return collegeRepository.findAll();
    }

    public List<CollegeImportResult> importCollegesFromFile(MultipartFile multipartFile) throws Exception {

        List<College> fileColleges = CollegeCsvImporter.parseFile(multipartFile);

        ArrayList<CollegeImportResult> collegeImportResults = new ArrayList<>();
        for (College college : fileColleges) {
            collegeImportResults.add(processFileRow(college));
        }
        return collegeImportResults;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected CollegeImportResult processFileRow(College college) {

        CollegeImportResult importResult = new CollegeImportResult();
        importResult.setCollegeShortName(college.getShortName());

        College dbCollege = collegeRepository.findByShortName(college.getShortName());
        if (dbCollege == null) {
            logger.debug("Creating the parsed college with short name '{}'.", college.getShortName());
            try {
                dbCollege = save(college);
                importResult.setImportStatus(CollegeImportResult.Status.CREATED);
            } catch (Exception e) {
                importResult.setImportStatus(CollegeImportResult.Status.ERROR);
                logger.error(e.getMessage(), e);
            }
        } else {
            logger.debug("College with short name '{}' already exists. Skipping import.", college.getShortName());
            importResult.setImportStatus(CollegeImportResult.Status.EXISTS);
        }
        importResult.setCollege(dbCollege);
        return importResult;
    }
}
