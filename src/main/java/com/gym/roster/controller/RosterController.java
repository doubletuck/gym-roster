package com.gym.roster.controller;

import com.gym.roster.parser.AthleteRosterCsvImporter;
import com.gym.roster.parser.AthleteRosterImportResult;
import com.gym.roster.service.AthleteService;
import com.gym.roster.service.CollegeService;
import com.gym.roster.service.RosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/roster/athlete")
public class RosterController {

    private final RosterService rosterService;
    private final AthleteService athleteService;
    private final CollegeService collegeService;

    @Autowired
    public RosterController(RosterService rosterService, CollegeService collegeService, AthleteService athleteService) {
        this.rosterService = rosterService;
        this.collegeService = collegeService;
        this.athleteService = athleteService;
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<AthleteRosterImportResult>> importRosterFromFile(@RequestParam MultipartFile file) {
        try {
            AthleteRosterCsvImporter importer = new AthleteRosterCsvImporter(collegeService, athleteService, rosterService);
            importer.parseFile(file);
            return ResponseEntity.ok(importer.getImportResults());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
