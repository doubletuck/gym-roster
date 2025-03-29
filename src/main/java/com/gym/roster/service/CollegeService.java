package com.gym.roster.service;

import com.gym.roster.domain.College;
import com.gym.roster.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;

    @Autowired
    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    public Optional<College> findById(UUID id) {
        return collegeRepository.findById(id);
    }

    public College save(College college) {
        Instant now = Instant.now();
        if (college.getId() == null) {
            college.setCreationTimestamp(now);
        }
        college.setLastUpdateTimestamp(now);
        return collegeRepository.save(college);
    }

    public void deleteById(UUID id) {
        collegeRepository.deleteById(id);
    }

    public Page<College> getPaginatedEntities(Pageable pageable) {
        return collegeRepository.findAll(pageable);
    }

    public List<College> findAll() {
        return collegeRepository.findAll();
    }
}
