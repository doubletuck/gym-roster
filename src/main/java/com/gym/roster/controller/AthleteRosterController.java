package com.gym.roster.controller;

import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.dto.AthleteRosterRequest;
import com.gym.roster.parser.AthleteRosterImporter;
import com.gym.roster.parser.AthleteRosterImportResult;
import com.gym.roster.service.AthleteService;
import com.gym.roster.service.CollegeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/roster/athlete")
public class AthleteRosterController {

    private static final Logger logger = LoggerFactory.getLogger(AthleteRosterController.class);

    private final AthleteService athleteService;
    private final CollegeService collegeService;

    @Autowired
    public AthleteRosterController(CollegeService collegeService, AthleteService athleteService) {
        this.collegeService = collegeService;
        this.athleteService = athleteService;
    }

    @PostMapping
    public ResponseEntity<AthleteRoster> create(@Valid @RequestBody AthleteRosterRequest request) {
        return athleteService.createRoster(request)
                .map(roster -> new ResponseEntity<>(roster, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        athleteService.deleteRosterById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<AthleteRosterImportResult>> importRosterFromFile(@RequestParam MultipartFile file)
            throws Exception {
        AthleteRosterImporter importer = new AthleteRosterImporter(collegeService, athleteService);
        List<AthleteRosterImportResult> results = importer.parseFile(file);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/directory-import")
    public ResponseEntity<Boolean> importRosterFromDirectory(@RequestParam String directoryPath)
            throws Exception {
        AthleteRosterImporter importer = new AthleteRosterImporter(collegeService, athleteService);
        importer.parseDirectory(directoryPath);
        return ResponseEntity.ok(true);
    }
}
