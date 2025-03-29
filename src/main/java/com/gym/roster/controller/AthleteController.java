package com.gym.roster.controller;

import com.gym.roster.domain.Athlete;
import com.gym.roster.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/athlete")
public class AthleteController {

    private final AthleteService athleteService;

    @Autowired
    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Athlete> findById(@PathVariable UUID id) {
        return athleteService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Athlete> create(@RequestBody Athlete athlete) {
        Athlete createdAthlete = athleteService.save(athlete);
        return new ResponseEntity<>(createdAthlete, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Athlete> update(@PathVariable UUID id, @RequestBody Athlete athlete) {
        Optional<Athlete> existingAthlete = athleteService.findById(id);
        if (existingAthlete.isPresent()) {
            Athlete updatedAthlete = existingAthlete.get();
            updatedAthlete.setClubName(athlete.getClubName());
            updatedAthlete.setFirstName(athlete.getFirstName());
            updatedAthlete.setLastName(athlete.getLastName());
            updatedAthlete.setHomeCountry(athlete.getHomeCountry());
            updatedAthlete.setHomeState(athlete.getHomeState());
            updatedAthlete.setHomeTown(athlete.getHomeTown());
            return new ResponseEntity<>(athleteService.save(updatedAthlete), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        athleteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<Athlete>> getPaginatedEntities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Athlete> athletes = athleteService.getPaginatedEntities(pageable);
        return ResponseEntity.ok(athletes);
    }
}