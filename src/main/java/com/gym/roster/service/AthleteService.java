package com.gym.roster.service;

import com.gym.roster.domain.Athlete;
import com.gym.roster.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;

    @Autowired
    public AthleteService(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    public Optional<Athlete> findById(UUID id) {
        return athleteRepository.findById(id);
    }

    public Athlete findByNameAndHomeCity(String firstName, String lastName, String homeCity) {
        return athleteRepository.findByNameAndHomeCity(firstName, lastName, homeCity);
    }

    public Athlete save(Athlete athlete) {
        Instant now = Instant.now();
        if (athlete.getId() == null) {
            athlete.setCreationTimestamp(now);
        }
        athlete.setLastUpdateTimestamp(now);
        return athleteRepository.save(athlete);
    }

    public void deleteById(UUID id) {
        athleteRepository.deleteById(id);
    }

    public Page<Athlete> getPaginatedEntities(Pageable pageable) {
        return athleteRepository.findAll(pageable);
    }
}