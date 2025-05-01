package com.gym.roster.service;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.College;
import com.gym.roster.repository.AthleteRosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AthleteRosterService {

    private final AthleteRosterRepository athleteRosterRepository;

    @Autowired
    public AthleteRosterService(AthleteRosterRepository memberRepository) {
        this.athleteRosterRepository = memberRepository;
    }

    public List<AthleteRoster> findAll() {
        return athleteRosterRepository.findAll();
    }

    public Optional<AthleteRoster> findById(UUID id) {
        return athleteRosterRepository.findById(id);
    }

    public AthleteRoster findByYearCollegeAndAthlete(Short seasonYear, College college, Athlete athlete) {
        return athleteRosterRepository.findByYearCollegeAndAthlete(seasonYear, college, athlete);
    }

    public AthleteRoster save(AthleteRoster member) {
        Instant now = Instant.now();
        if (member.getId() == null) {
            member.setCreationTimestamp(now);
        }
        member.setLastUpdateTimestamp(now);
        return athleteRosterRepository.save(member);
    }

    public void deleteById(UUID id) {
        athleteRosterRepository.deleteById(id);
    }

    public Page<AthleteRoster> getPaginatedEntities(Pageable pageable) {
        return athleteRosterRepository.findAll(pageable);
    }

}
