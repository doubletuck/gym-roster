package com.gym.roster.controller;

import com.gym.roster.parser.MeetImportResult;
import com.gym.roster.parser.MeetImporter;
import com.gym.roster.service.AthleteRosterService;
import com.gym.roster.service.CollegeService;
import com.gym.roster.service.MeetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/meet")
public class MeetController {

    private final static Logger logger = LoggerFactory.getLogger(MeetController.class);

    private final CollegeService collegeService;
    private final MeetService meetService;
    private final AthleteRosterService athleteRosterService;

    @Autowired
    public MeetController(CollegeService collegeService, MeetService meetService, AthleteRosterService athleteRosterService) {
        this.collegeService = collegeService;
        this.meetService = meetService;
        this.athleteRosterService = athleteRosterService;
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<MeetImportResult>> importMeetFromFile(@RequestParam MultipartFile file) {
        try {
            MeetImporter importer = new MeetImporter(collegeService, athleteRosterService, meetService);
            List<MeetImportResult> results = importer.parseFile(file);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error importing meet scores from a directory: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
}
