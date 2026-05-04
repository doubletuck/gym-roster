package com.gym.roster.repository;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AthleteRosterRepository extends JpaRepository<AthleteRoster, Long> {

    List<AthleteRoster> findByAthlete(Athlete athlete);

    List<AthleteRoster> findByAthleteIn(List<Athlete> athletes);

    @Query("SELECT r FROM AthleteRoster r WHERE r.seasonYear = ?1 AND r.college = ?2 AND r.athlete = ?3")
    AthleteRoster findByYearCollegeAndAthlete(Short seasonYear, College college, Athlete athlete);

    @Query("SELECT r FROM AthleteRoster r " +
            "WHERE r.seasonYear = ?1 " +
            "AND (LOWER(r.college.codeName) = LOWER(?2) OR LOWER(r.college.shortName) = LOWER(?2)) " +
            "AND LOWER(r.athlete.firstName) = LOWER(?3) " +
            "AND LOWER(r.athlete.lastName) = LOWER(?4)")
    AthleteRoster findByYearCollegeNameAndAthleteName(Short seasonYear, String collegeName,
            String athleteFirstName, String athleteLastName);
}
