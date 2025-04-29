package com.gym.roster.service;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.College;
import com.gym.roster.domain.Roster;
import com.gym.roster.repository.RosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RosterService {

    private final RosterRepository rosterRepository;

    @Autowired
    public RosterService(RosterRepository memberRepository) {
        this.rosterRepository = memberRepository;
    }

    public List<Roster> findAll() {
        return rosterRepository.findAll();
    }

    public Optional<Roster> findById(UUID id) {
        return rosterRepository.findById(id);
    }

    public Roster findByYearCollegeAndAthlete(Short seasonYear, College college, Athlete athlete) {
        return rosterRepository.findByYearCollegeAndAthlete(seasonYear, college, athlete);
    }

    public Roster save(Roster member) {
        Instant now = Instant.now();
        if (member.getId() == null) {
            member.setCreationTimestamp(now);
        }
        member.setLastUpdateTimestamp(now);
        return rosterRepository.save(member);
    }

    public void deleteById(UUID id) {
        rosterRepository.deleteById(id);
    }

    public Page<Roster> getPaginatedEntities(Pageable pageable) {
        return rosterRepository.findAll(pageable);
    }

}
