package com.gym.roster.repository;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AthleteRosterRepository extends JpaRepository<AthleteRoster, UUID> {

    @Query("SELECT r FROM AthleteRoster r WHERE r.seasonYear = ?1 AND r.college = ?2 AND r.athlete = ?3")
    AthleteRoster findByYearCollegeAndAthlete(Short seasonYear, College college, Athlete athlete);
}
