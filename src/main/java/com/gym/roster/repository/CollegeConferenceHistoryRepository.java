package com.gym.roster.repository;

import com.gym.roster.domain.CollegeConferenceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollegeConferenceHistoryRepository extends JpaRepository<CollegeConferenceHistory, UUID> {
}
