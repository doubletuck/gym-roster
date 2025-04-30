package com.gym.roster.controller;

import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.parser.CoachRosterImportResult;
import com.gym.roster.service.CoachRosterService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roster/coach")
public class CoachRosterController {

    private final CoachRosterService coachRosterService;

    @Autowired
    public CoachRosterController(CoachRosterService coachRosterService) {
        this.coachRosterService = coachRosterService;
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

    @DeleteMapping("/{seasonYear}/{collegeCodeName}")
    public ResponseEntity<Void> deleteCollegeCoachRosterForSeason(
            @PathVariable Short seasonYear,
            @PathVariable String collegeCodeName) {
        //TODO
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/file-import")
    public ResponseEntity<List<CoachRosterImportResult>> importRosterFromFile(@RequestParam MultipartFile file) {
        try {
            List<CoachRosterImportResult> results = new ArrayList<>();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}