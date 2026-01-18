package com.gym.roster.service;

import com.doubletuck.gym.common.model.Event;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.Meet;
import com.gym.roster.domain.MeetScore;
import com.gym.roster.repository.MeetRepository;
import com.gym.roster.repository.MeetScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MeetService {

    private final MeetRepository meetRepository;
    private final MeetScoreRepository meetScoreRepository;

    @Autowired
    public MeetService(MeetRepository meetRepository, MeetScoreRepository meetScoreRepository) {
        this.meetRepository = meetRepository;
        this.meetScoreRepository = meetScoreRepository;
    }

    public Optional<Meet> findById(Long id) {
        return meetRepository.findById(id);
    }

    public Meet findByDateAndName(LocalDate meetDate, String meetName) {
        return meetRepository.findByEventDateAndEventNameIgnoreCase(meetDate, meetName);
    }

    public MeetScore findScoreByMeetAthleteRosterAndEvent(Meet meet, AthleteRoster athleteRoster, Event event) {
        return meetScoreRepository.findByMeetAthleteRosterAndEvent(meet, athleteRoster, event)  ;
    };

    public Meet saveMeet(Meet meet) {
        return meetRepository.save(meet);
    }

    public MeetScore saveMeetScore(MeetScore meetScore) {
        return meetScoreRepository.save(meetScore);
    }
}
