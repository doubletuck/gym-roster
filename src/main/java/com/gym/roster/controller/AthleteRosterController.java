package com.gym.roster.controller;

import com.gym.roster.parser.AthleteRosterImporter;
import com.gym.roster.parser.AthleteRosterImportResult;
import com.gym.roster.service.AthleteService;
import com.gym.roster.service.CollegeService;
import com.gym.roster.service.AthleteRosterService;
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
public class AthleteRosterController {

    private final AthleteRosterService athleteRosterService;
    private final AthleteService athleteService;
    private final CollegeService collegeService;

    @Autowired
    public AthleteRosterController(AthleteRosterService athleteRosterService, CollegeService collegeService,
            AthleteService athleteService) {
        this.athleteRosterService = athleteRosterService;
        this.collegeService = collegeService;
        this.athleteService = athleteService;
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<AthleteRosterImportResult>> importRosterFromFile(@RequestParam MultipartFile file)
            throws Exception {
        AthleteRosterImporter importer = new AthleteRosterImporter(collegeService, athleteService,
                athleteRosterService);
        importer.parseFile(file);
        return ResponseEntity.ok(importer.getImportResults());
    }

    @PostMapping("/directory-import")
    public ResponseEntity<Boolean> importRosterFromDirectory(@RequestParam String directoryPath)
            throws Exception {
        AthleteRosterImporter importer = new AthleteRosterImporter(collegeService, athleteService,
                athleteRosterService);
        importer.parseDirectory(directoryPath);
        return ResponseEntity.ok(true);
    }
}
