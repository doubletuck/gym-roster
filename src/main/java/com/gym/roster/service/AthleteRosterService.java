package com.gym.roster.service;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.College;
import com.gym.roster.repository.AthleteRosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<AthleteRoster> findById(Long id) {
        return athleteRosterRepository.findById(id);
    }

    public AthleteRoster findByYearCollegeAndAthlete(Short seasonYear, College college, Athlete athlete) {
        return athleteRosterRepository.findByYearCollegeAndAthlete(seasonYear, college, athlete);
    }

    public AthleteRoster save(AthleteRoster member) {
        return athleteRosterRepository.save(member);
    }

    public void deleteById(Long id) {
        athleteRosterRepository.deleteById(id);
    }

    public Page<AthleteRoster> getPaginatedEntities(Pageable pageable) {
        return athleteRosterRepository.findAll(pageable);
    }
}
