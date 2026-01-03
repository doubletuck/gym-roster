package com.gym.roster.repository;

import com.gym.roster.domain.MeetScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetScoreRepository extends JpaRepository<MeetScore, Long> {
}
