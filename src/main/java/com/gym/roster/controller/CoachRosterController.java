package com.gym.roster.controller;

import com.gym.roster.domain.CoachRoster;
import com.gym.roster.parser.CoachRosterImporter;
import com.gym.roster.parser.CoachRosterImportResult;
import com.gym.roster.service.CoachRosterService;
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
import java.util.UUID;

@RestController
@RequestMapping("/roster/coach")
public class CoachRosterController {

    private final static Logger logger = LoggerFactory.getLogger(CoachRosterController.class);

    private final CoachRosterService coachRosterService;
    private final CollegeService collegeService;
    private final CoachService coachService;

    @Autowired
    public CoachRosterController(CoachRosterService coachRosterService, CollegeService collegeService,
            CoachService coachService) {
        this.coachRosterService = coachRosterService;
        this.collegeService = collegeService;
        this.coachService = coachService;
    }

    @PostMapping
    public ResponseEntity<CoachRoster> create(@RequestBody CoachRoster coachRoster) {
        CoachRoster createdCoachRoster = coachRosterService.save(coachRoster);
        return ResponseEntity.ok(createdCoachRoster);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachRoster> findById(@PathVariable UUID id) {
        return coachRosterService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<List<CoachRoster>> getCollegeCoachRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        return ResponseEntity.ok(coachRosterService.findByYearAndCollegeCode(seasonYear, collegeCodeName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        coachRosterService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<Void> deleteCollegeCoachRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        coachRosterService.deleteByYearAndCollegeCodeName(seasonYear, collegeCodeName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<CoachRosterImportResult>> importRosterFromFile(@RequestParam MultipartFile file) {
        try {
            CoachRosterImporter importer = new CoachRosterImporter(collegeService, coachService,
                    coachRosterService);
            importer.parseFile(file);
            return ResponseEntity.ok(importer.getImportResults());
        } catch (Exception e) {
            logger.error("Error importing coach roster file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}