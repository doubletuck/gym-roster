package com.gym.roster.service;

import com.gym.roster.domain.Meet;
import com.gym.roster.repository.MeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetService {

    private final MeetRepository meetRepository;

    @Autowired
    public MeetService(MeetRepository meetRepository) {
        this.meetRepository = meetRepository;
    }

    public Optional<Meet> findById(Long id) {
        return meetRepository.findById(id);
    }

    public Meet saveMeet(Meet meet) {
        return meetRepository.save(meet);
    }
}
