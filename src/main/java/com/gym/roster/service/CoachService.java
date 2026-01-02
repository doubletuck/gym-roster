package com.gym.roster.service;

import com.gym.roster.domain.Coach;
import com.gym.roster.repository.CoachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CoachService {

    private final CoachRepository coachRepository;

    @Autowired
    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public Optional<Coach> findById(Long id) {
        return coachRepository.findById(id);
    }

    public Coach findByName(String firstName, String lastName) {
        return coachRepository.findByName(firstName, lastName);
    }

    public Coach save(Coach coach) {
        return coachRepository.save(coach);
    }

    public void deleteById(Long id) {
        coachRepository.deleteById(id);
    }

    public Page<Coach> getPaginatedEntities(Pageable pageable) {
        return coachRepository.findAll(pageable);
    }
}
