package com.gym.roster.repository;

import com.gym.roster.domain.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, UUID> {

    @Query("SELECT a FROM Athlete a " +
            "WHERE UPPER(a.firstName) = UPPER(?1) " +
            "AND UPPER(a.lastName) = UPPER(?2) " +
            "AND UPPER(a.homeCity) = UPPER(?3)")
    Athlete findByNameAndHomeCity(String firstName, String lastName, String homeCity);
}
