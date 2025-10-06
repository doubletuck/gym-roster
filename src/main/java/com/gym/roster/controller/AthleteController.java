package com.gym.roster.controller;

import com.gym.roster.domain.Athlete;
import com.gym.roster.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/athlete")
public class AthleteController {

    private final AthleteService athleteService;
    private final PagedResourcesAssembler<Athlete> pagedResourcesAssembler;

    @Autowired
    public AthleteController(AthleteService athleteService, PagedResourcesAssembler<Athlete> pagedResourcesAssembler) {
        this.athleteService = athleteService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Athlete> findById(@PathVariable UUID id) {
        return athleteService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Athlete> create(@RequestBody Athlete athlete) {
        Athlete createdAthlete = athleteService.save(athlete);
        return new ResponseEntity<>(createdAthlete, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Athlete> update(@PathVariable UUID id, @RequestBody Athlete athlete) {
        return athleteService.findById(id)
                .map(existingAthlete -> {
                    existingAthlete.setClubName(athlete.getClubName());
                    existingAthlete.setFirstName(athlete.getFirstName());
                    existingAthlete.setLastName(athlete.getLastName());
                    existingAthlete.setHomeCountry(athlete.getHomeCountry());
                    existingAthlete.setHomeState(athlete.getHomeState());
                    existingAthlete.setHomeCity(athlete.getHomeCity());
                    return ResponseEntity.ok(athleteService.save(existingAthlete));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        athleteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Athlete>>> getPaginatedEntities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Athlete> athletes = athleteService.getPaginatedEntities(pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(athletes));
    }
}