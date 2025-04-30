package com.gym.roster.repository;

import com.gym.roster.domain.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoachRepository extends JpaRepository<Coach, UUID> {

    @Query("SELECT c FROM Coach c " +
            "WHERE UPPER(c.firstName) = UPPER(?1) " +
            "AND UPPER(c.lastName) = UPPER(?2) ")
    Coach findByName(String firstName, String lastName);
}
