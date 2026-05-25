package com.gym.roster.controller;

import com.gym.roster.domain.Coach;
import com.gym.roster.dto.CoachFilterParams;
import com.gym.roster.dto.CoachResponse;
import com.gym.roster.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@RestController
@RequestMapping("/coach")
public class CoachController {

    private final CoachService coachService;
    private final PagedResourcesAssembler<CoachResponse> pagedResourcesAssembler;

    @Autowired
    public CoachController(CoachService coachService, PagedResourcesAssembler<CoachResponse> pagedResourcesAssembler) {
        this.coachService = coachService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coach> findById(@PathVariable Long id) {
        return coachService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Coach> create(@RequestBody Coach coach) {
        Coach createdCoach = coachService.save(coach);
        return new ResponseEntity<>(createdCoach, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coach> update(@PathVariable Long id, @RequestBody Coach coach) {
        return coachService.findById(id)
                .map(existingCoach -> {
                    existingCoach.setFirstName(coach.getFirstName());
                    existingCoach.setLastName(coach.getLastName());
                    return ResponseEntity.ok(coachService.save(existingCoach));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        coachService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CoachResponse>>> getPaginatedEntities(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String collegeCodeName,
            @RequestParam(required = false) Short seasonYear) {
        CoachFilterParams filterParams = new CoachFilterParams(q, firstName, lastName, collegeCodeName, seasonYear);
        Page<CoachResponse> coaches = coachService.getPaginatedEntities(filterParams, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(coaches));
    }
}
