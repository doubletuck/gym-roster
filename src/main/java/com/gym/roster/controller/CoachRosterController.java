package com.gym.roster.controller;

import com.gym.roster.domain.CoachRoster;
import com.gym.roster.parser.CoachRosterImporter;
import com.gym.roster.parser.CoachRosterImportResult;
import com.gym.roster.service.CoachService;
import com.gym.roster.service.CollegeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/roster/coach")
public class CoachRosterController {

    private final static Logger logger = LoggerFactory.getLogger(CoachRosterController.class);

    private final CollegeService collegeService;
    private final CoachService coachService;

    @Autowired
    public CoachRosterController(CollegeService collegeService, CoachService coachService) {
        this.collegeService = collegeService;
        this.coachService = coachService;
    }

    @PostMapping
    public ResponseEntity<CoachRoster> create(@RequestBody CoachRoster coachRoster) {
        CoachRoster createdCoachRoster = coachService.save(coachRoster);
        return ResponseEntity.ok(createdCoachRoster);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachRoster> findById(@PathVariable Long id) {
        return coachService.findRosterById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<List<CoachRoster>> getCollegeCoachRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        return ResponseEntity.ok(coachService.findRosterByYearAndCollegeCode(seasonYear, collegeCodeName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        coachService.deleteRosterById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<Void> deleteCollegeCoachRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        coachService.deleteRosterByYearAndCollegeCodeName(seasonYear, collegeCodeName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<CoachRosterImportResult>> importRosterFromFile(@RequestParam MultipartFile file) {
        try {
            CoachRosterImporter importer = new CoachRosterImporter(collegeService, coachService);
            List<CoachRosterImportResult> results = importer.parseFile(file);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error importing coach roster file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/directory-import")
    public ResponseEntity<Boolean> importRosterFromDirectory(@RequestParam String directoryPath) {
        try {
            CoachRosterImporter importer = new CoachRosterImporter(collegeService, coachService);
            importer.parseDirectory(directoryPath);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            logger.error("Error importing coach roster file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}