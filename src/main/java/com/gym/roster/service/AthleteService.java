package com.gym.roster.service;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.dto.AthleteDto;
import com.gym.roster.dto.AthleteFilterParams;
import com.gym.roster.repository.AthleteRepository;
import com.gym.roster.repository.AthleteRosterRepository;
import com.gym.roster.specification.AthleteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;
    private final AthleteRosterRepository athleteRosterRepository;

    @Autowired
    public AthleteService(AthleteRepository athleteRepository, AthleteRosterRepository athleteRosterRepository) {
        this.athleteRepository = athleteRepository;
        this.athleteRosterRepository = athleteRosterRepository;
    }

    public Optional<Athlete> findById(Long id) {
        return athleteRepository.findById(id);
    }

    public Optional<AthleteDto> findDtoById(Long id) {
        return athleteRepository.findById(id)
                .map(athlete -> AthleteDto.from(athlete, athleteRosterRepository.findByAthlete(athlete)));
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

    public Page<AthleteDto> getPaginatedEntities(AthleteFilterParams params, Pageable pageable) {
        if (params.academicYear() != null && !params.academicYear().isBlank() && params.seasonYear() == null) {
            throw new IllegalArgumentException("academicYear requires seasonYear");
        }
        Page<Athlete> athletePage = athleteRepository.findAll(AthleteSpecification.build(params), pageable);
        List<Athlete> athletes = athletePage.getContent();
        Map<Long, List<AthleteRoster>> rostersByAthleteId = athleteRosterRepository.findByAthleteIn(athletes)
                .stream()
                .collect(Collectors.groupingBy(r -> r.getAthlete().getId()));
        return athletePage.map(athlete ->
                AthleteDto.from(athlete, rostersByAthleteId.getOrDefault(athlete.getId(), List.of())));
    }
}