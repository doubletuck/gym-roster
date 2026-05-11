package com.gym.roster.service;

import com.doubletuck.gym.common.model.AcademicYear;
import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.College;
import com.gym.roster.dto.AthleteResponse;
import com.gym.roster.dto.AthleteFilterParams;
import com.gym.roster.dto.AthleteRequest;
import com.gym.roster.dto.AthleteRosterRequest;
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
    private final CollegeService collegeService;

    @Autowired
    public AthleteService(AthleteRepository athleteRepository, AthleteRosterRepository athleteRosterRepository,
            CollegeService collegeService) {
        this.athleteRepository = athleteRepository;
        this.athleteRosterRepository = athleteRosterRepository;
        this.collegeService = collegeService;
    }

    public Optional<Athlete> findById(Long id) {
        return athleteRepository.findById(id);
    }

    public Optional<AthleteResponse> findDtoById(Long id) {
        return athleteRepository.findById(id)
                .map(athlete -> AthleteResponse.from(athlete, athleteRosterRepository.findByAthlete(athlete)));
    }

    public Athlete findByNameAndHomeCity(String firstName, String lastName, String homeCity) {
        return athleteRepository.findByNameAndHomeCity(firstName, lastName, homeCity);
    }

    public Athlete create(AthleteRequest request) {
        Athlete athlete = new Athlete();
        athlete.setFirstName(request.firstName());
        athlete.setLastName(request.lastName());
        athlete.setHomeCity(request.homeCity());
        athlete.setHomeState(request.homeState());
        athlete.setHomeCountry(request.homeCountry());
        athlete.setClubName(request.clubName());
        return athleteRepository.save(athlete);
    }

    public Optional<Athlete> update(Long id, AthleteRequest request) {
        return athleteRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(request.firstName());
                    existing.setLastName(request.lastName());
                    existing.setHomeCity(request.homeCity());
                    existing.setHomeState(request.homeState());
                    existing.setHomeCountry(request.homeCountry());
                    existing.setClubName(request.clubName());
                    return athleteRepository.save(existing);
                });
    }

    public Optional<AthleteRoster> createRoster(AthleteRosterRequest request) {
        Optional<College> college = collegeService.findById(request.collegeId());
        if (college.isEmpty()) {
            return Optional.empty();
        }
        Optional<Athlete> athlete = athleteRepository.findById(request.athleteId());
        if (athlete.isEmpty()) {
            return Optional.empty();
        }
        AthleteRoster roster = new AthleteRoster();
        roster.setCollege(college.get());
        roster.setAthlete(athlete.get());
        roster.setSeasonYear(request.seasonYear());
        roster.setAcademicYear(AcademicYear.find(request.academicYear()));
        roster.setEvents(request.events());
        return Optional.of(athleteRosterRepository.save(roster));
    }

    public Athlete save(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public void deleteById(Long id) {
        athleteRepository.deleteById(id);
    }

    public List<AthleteRoster> findAllRosters() {
        return athleteRosterRepository.findAll();
    }

    public Optional<AthleteRoster> findRosterById(Long id) {
        return athleteRosterRepository.findById(id);
    }

    public AthleteRoster findRosterByYearCollegeAndAthlete(Short seasonYear, College college, Athlete athlete) {
        return athleteRosterRepository.findByYearCollegeAndAthlete(seasonYear, college, athlete);
    }

    public AthleteRoster findRosterByYearCollegeNameAndAthleteName(
            Short seasonYear,
            String collegeName,
            String athleteFirstName,
            String athleteLastName) {
        return athleteRosterRepository.findByYearCollegeNameAndAthleteName(seasonYear, collegeName, athleteFirstName,
                athleteLastName);
    }

    public AthleteRoster save(AthleteRoster roster) {
        return athleteRosterRepository.save(roster);
    }

    public void deleteRosterById(Long id) {
        athleteRosterRepository.deleteById(id);
    }

    public Page<AthleteRoster> getPaginatedRosters(Pageable pageable) {
        return athleteRosterRepository.findAll(pageable);
    }

    public Page<AthleteResponse> getPaginatedEntities(AthleteFilterParams params, Pageable pageable) {
        if (params.academicYear() != null && !params.academicYear().isBlank() && params.seasonYear() == null) {
            throw new IllegalArgumentException("academicYear requires seasonYear");
        }
        Page<Athlete> athletePage = athleteRepository.findAll(AthleteSpecification.build(params), pageable);
        List<Athlete> athletes = athletePage.getContent();
        Map<Long, List<AthleteRoster>> rostersByAthleteId = athleteRosterRepository.findByAthleteIn(athletes)
                .stream()
                .collect(Collectors.groupingBy(r -> r.getAthlete().getId()));
        return athletePage.map(
                athlete -> AthleteResponse.from(athlete, rostersByAthleteId.getOrDefault(athlete.getId(), List.of())));
    }
}