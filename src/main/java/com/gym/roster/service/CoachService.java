package com.gym.roster.service;

import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.dto.CoachFilterParams;
import com.gym.roster.dto.CoachResponse;
import com.gym.roster.repository.CoachRepository;
import com.gym.roster.repository.CoachRosterRepository;
import com.gym.roster.specification.CoachSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoachService {

    private final CoachRepository coachRepository;
    private final CoachRosterRepository coachRosterRepository;

    @Autowired
    public CoachService(CoachRepository coachRepository, CoachRosterRepository coachRosterRepository) {
        this.coachRepository = coachRepository;
        this.coachRosterRepository = coachRosterRepository;
    }

    public Optional<Coach> findById(Long id) {
        return coachRepository.findById(id);
    }

    public Optional<CoachResponse> findDtoById(Long id) {
        return coachRepository.findById(id)
                .map(coach -> CoachResponse.from(coach, coachRosterRepository.findByCoach(coach)));
    }

    public Coach findByName(String firstName, String lastName) {
        return coachRepository.findByName(firstName, lastName);
    }

    public Coach save(Coach coach) {
        return coachRepository.save(coach);
    }

    @Transactional
    public void deleteById(Long id) {
        coachRosterRepository.deleteByCoachId(id);
        coachRepository.deleteById(id);
    }

    public Page<CoachResponse> getPaginatedEntities(CoachFilterParams params, Pageable pageable) {
        Page<Coach> coachPage = coachRepository.findAll(CoachSpecification.build(params), pageable);
        List<Coach> coaches = coachPage.getContent();
        Map<Long, List<CoachRoster>> rostersByCoachId = coachRosterRepository.findByCoachIn(coaches)
                .stream()
                .collect(Collectors.groupingBy(r -> r.getCoach().getId()));
        return coachPage.map(
                coach -> CoachResponse.from(coach, rostersByCoachId.getOrDefault(coach.getId(), List.of())));
    }

    public Optional<CoachRoster> findRosterById(Long id) {
        return coachRosterRepository.findById(id);
    }

    public CoachRoster findRosterByYearAndCollegeAndCoach(Short seasonYear, College college, Coach coach) {
        return coachRosterRepository.findByYearAndCollegeAndCoach(seasonYear, college, coach);
    }

    public List<CoachRoster> findRosterByYearAndCollegeCode(Short seasonYear, String collegeCodeName) {
        return coachRosterRepository.findByYearAndCollegeCodeName(seasonYear, collegeCodeName);
    }

    public CoachRoster save(CoachRoster roster) {
        return coachRosterRepository.save(roster);
    }

    public void deleteRosterById(Long id) {
        coachRosterRepository.deleteById(id);
    }

    public void deleteRosterByYearAndCollegeCodeName(Short seasonYear, String collegeCodeName) {
        coachRosterRepository.deleteByYearAndCollegeCodeName(seasonYear, collegeCodeName);
    }

    public Page<CoachRoster> getPaginatedRosters(Pageable pageable) {
        return coachRosterRepository.findAll(pageable);
    }
}
