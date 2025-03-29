package com.gym.roster.service;

import com.gym.roster.domain.CollegeConferenceHistory;
import com.gym.roster.repository.CollegeConferenceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CollegeConferenceHistoryService {

    private final CollegeConferenceHistoryRepository collegeConferenceHistoryRepository;

    @Autowired
    public CollegeConferenceHistoryService(CollegeConferenceHistoryRepository collegeConferenceHistoryRepository) {
        this.collegeConferenceHistoryRepository = collegeConferenceHistoryRepository;
    }

    public Optional<CollegeConferenceHistory> findById(UUID id) {
        return collegeConferenceHistoryRepository.findById(id);
    }

    public CollegeConferenceHistory save(CollegeConferenceHistory entity) {
        return collegeConferenceHistoryRepository.save(entity);
    }

    public void deleteById(UUID id) {
        collegeConferenceHistoryRepository.deleteById(id);
    }
}
