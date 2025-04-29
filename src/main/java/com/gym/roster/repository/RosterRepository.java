package com.gym.roster.repository;

import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.College;
import com.gym.roster.domain.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RosterRepository extends JpaRepository<Roster, UUID> {

    @Query("SELECT r FROM Roster r WHERE r.seasonYear = ?1 AND r.college = ?2 AND r.athlete = ?3")
    Roster findByYearCollegeAndAthlete(Short seasonYear, College college, Athlete athlete);
}
