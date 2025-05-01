package com.gym.roster.service;

import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.repository.CoachRosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachRosterService {

    private final CoachRosterRepository coachRosterRepository;

    @Autowired
    public CoachRosterService(CoachRosterRepository coachRosterRepository) {
        this.coachRosterRepository = coachRosterRepository;
    }

    public Optional<CoachRoster> findById(UUID id) {
        return coachRosterRepository.findById(id);
    }

    public CoachRoster findByYearAndCollegeAndCoach(Short seasonYear, College college, Coach coach) {
        return coachRosterRepository.findByYearAndCollegeAndCoach(seasonYear, college, coach);
    }

    public List<CoachRoster> findByYearAndCollegeCode(Short seasonYear, String collegeCodeName) {
        return coachRosterRepository.findByYearAndCollegeCodeName(seasonYear, collegeCodeName);
    }

    public CoachRoster save(CoachRoster roster) {
        Instant now = Instant.now();
        if (roster.getId() == null) {
            roster.setCreationTimestamp(now);
        }
        roster.setLastUpdateTimestamp(now);
        return coachRosterRepository.save(roster);
    }

    public void deleteById(UUID id) {
        coachRosterRepository.deleteById(id);
    }

    public void deleteByYearAndCollegeCodeName(Short seasonYear, String collegeCodeName) {
        coachRosterRepository.deleteByYearAndCollegeCodeName(seasonYear, collegeCodeName);
    }

    public Page<CoachRoster> getPaginatedEntities(Pageable pageable) {
        return coachRosterRepository.findAll(pageable);
    }
}
