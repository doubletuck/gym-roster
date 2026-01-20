package com.gym.roster.repository;

import com.doubletuck.gym.common.model.Event;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.Meet;
import com.gym.roster.domain.MeetScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetScoreRepository extends JpaRepository<MeetScore, Long> {

    @Query("SELECT ms FROM MeetScore ms WHERE ms.meet = ?1 AND ms.athleteRoster = ?2 AND ms.event = ?3")
    MeetScore findByMeetAthleteRosterAndEvent(Meet meet, AthleteRoster athleteRoster, Event event);
}
