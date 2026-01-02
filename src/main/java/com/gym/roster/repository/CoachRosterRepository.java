package com.gym.roster.repository;

import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CoachRosterRepository extends JpaRepository<CoachRoster, Long> {

    @Query("SELECT r FROM CoachRoster r WHERE r.seasonYear = ?1 AND r.college = ?2 AND r.coach = ?3")
    CoachRoster findByYearAndCollegeAndCoach(Short seasonYear, College college, Coach coach);

    @Query("SELECT r FROM CoachRoster r WHERE r.seasonYear = ?1 AND UPPER(r.college.codeName) = UPPER(?2)")
    List<CoachRoster> findByYearAndCollegeCodeName(Short seasonYear, String collegeCodeName);

    @Modifying
    @Transactional
    @Query("DELETE FROM CoachRoster r WHERE r.seasonYear = ?1 AND UPPER(r.college.codeName) = UPPER(?2)")
    void deleteByYearAndCollegeCodeName(Short year, String collegeCodeName);

}
