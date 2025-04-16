package com.gym.roster.repository;

import com.gym.roster.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollegeRepository extends JpaRepository<College, UUID> {

    @Query("SELECT c FROM College c WHERE c.codeName = ?1")
    College findByCodeName(String collegeCodeName);
}
