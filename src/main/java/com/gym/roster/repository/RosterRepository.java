package com.gym.roster.repository;

import com.gym.roster.domain.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RosterRepository extends JpaRepository<Roster, UUID> {
}
