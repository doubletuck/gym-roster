package com.gym.roster.service;

import com.gym.roster.domain.Athlete;
import com.gym.roster.dto.AthleteFilterParams;
import com.gym.roster.repository.AthleteRepository;
import com.gym.roster.specification.AthleteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;

    @Autowired
    public AthleteService(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    public Optional<Athlete> findById(Long id) {
        return athleteRepository.findById(id);
    }

    public Athlete findByNameAndHomeCity(String firstName, String lastName, String homeCity) {
        return athleteRepository.findByNameAndHomeCity(firstName, lastName, homeCity);
    }

    public Athlete save(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public void deleteById(Long id) {
        athleteRepository.deleteById(id);
    }

    public Page<Athlete> getPaginatedEntities(AthleteFilterParams params, Pageable pageable) {
        if (params.academicYear() != null && !params.academicYear().isBlank() && params.seasonYear() == null) {
            throw new IllegalArgumentException("academicYear requires seasonYear");
        }
        return athleteRepository.findAll(AthleteSpecification.build(params), pageable);
    }
}