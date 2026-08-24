package com.gym.roster.controller;

import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.dto.AthleteRosterRequest;
import com.gym.roster.dto.CollegeRosterResponse;
import com.gym.roster.dto.CoachRosterRequest;
import com.gym.roster.parser.AthleteRosterImporter;
import com.gym.roster.parser.AthleteRosterImportResult;
import com.gym.roster.parser.CoachRosterImporter;
import com.gym.roster.parser.CoachRosterImportResult;
import com.gym.roster.service.AthleteService;
import com.gym.roster.service.CoachService;
import com.gym.roster.service.CollegeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/roster")
public class RosterController {

    private static final Logger logger = LoggerFactory.getLogger(RosterController.class);

    private final CollegeService collegeService;
    private final AthleteService athleteService;
    private final CoachService coachService;

    @Autowired
    public RosterController(CollegeService collegeService, AthleteService athleteService, CoachService coachService) {
        this.collegeService = collegeService;
        this.athleteService = athleteService;
        this.coachService = coachService;
    }

    @GetMapping("/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<CollegeRosterResponse> getCollegeRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        College college = collegeService.findByCodeName(collegeCodeName);
        if (college == null) {
            return ResponseEntity.notFound().build();
        }
        List<AthleteRoster> athletes = athleteService.findRosterByYearAndCollegeCode(seasonYear, collegeCodeName);
        List<CoachRoster> coaches = coachService.findRosterByYearAndCollegeCode(seasonYear, collegeCodeName);
        return ResponseEntity.ok(CollegeRosterResponse.from(college, seasonYear, athletes, coaches));
    }

    @PostMapping("/athlete")
    public ResponseEntity<AthleteRoster> createAthleteRoster(@Valid @RequestBody AthleteRosterRequest request) {
        return athleteService.createRoster(request)
                .map(roster -> new ResponseEntity<>(roster, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/athlete/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<List<AthleteRoster>> getCollegeAthleteRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        return ResponseEntity.ok(athleteService.findRosterByYearAndCollegeCode(seasonYear, collegeCodeName));
    }

    @DeleteMapping("/athlete/{id}")
    public ResponseEntity<Void> deleteAthleteRosterById(@PathVariable Long id) {
        athleteService.deleteRosterById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/athlete/file-import")
    public ResponseEntity<List<AthleteRosterImportResult>> importAthleteRosterFromFile(@RequestParam MultipartFile file)
            throws Exception {
        AthleteRosterImporter importer = new AthleteRosterImporter(collegeService, athleteService);
        List<AthleteRosterImportResult> results = importer.parseFile(file);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/athlete/directory-import")
    public ResponseEntity<Boolean> importAthleteRosterFromDirectory(@RequestParam String directoryPath)
            throws Exception {
        AthleteRosterImporter importer = new AthleteRosterImporter(collegeService, athleteService);
        importer.parseDirectory(directoryPath);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/coach")
    public ResponseEntity<CoachRoster> createCoachRoster(@Valid @RequestBody CoachRosterRequest request) {
        return coachService.createRoster(request)
                .map(roster -> new ResponseEntity<>(roster, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/coach/{id}")
    public ResponseEntity<CoachRoster> findCoachRosterById(@PathVariable Long id) {
        return coachService.findRosterById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/coach/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<List<CoachRoster>> getCollegeCoachRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        return ResponseEntity.ok(coachService.findRosterByYearAndCollegeCode(seasonYear, collegeCodeName));
    }

    @DeleteMapping("/coach/{id}")
    public ResponseEntity<Void> deleteCoachRosterById(@PathVariable Long id) {
        coachService.deleteRosterById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/coach/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<Void> deleteCollegeCoachRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        coachService.deleteRosterByYearAndCollegeCodeName(seasonYear, collegeCodeName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/coach/file-import")
    public ResponseEntity<List<CoachRosterImportResult>> importCoachRosterFromFile(@RequestParam MultipartFile file) {
        try {
            CoachRosterImporter importer = new CoachRosterImporter(collegeService, coachService);
            List<CoachRosterImportResult> results = importer.parseFile(file);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error importing coach roster file: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/coach/directory-import")
    public ResponseEntity<Boolean> importCoachRosterFromDirectory(@RequestParam String directoryPath) {
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
