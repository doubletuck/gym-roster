package com.gym.roster.service;

import com.gym.roster.domain.Roster;
import com.gym.roster.repository.RosterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RosterService {

    private final RosterRepository rosterRepository;

    @Autowired
    public RosterService(RosterRepository memberRepository) {
        this.rosterRepository = memberRepository;
    }

    public List<Roster> findAll() {
        return rosterRepository.findAll();
    }

    public Optional<Roster> findById(UUID id) {
        return rosterRepository.findById(id);
    }

    public Roster save(Roster member) {
        return rosterRepository.save(member);
    }

    public void deleteById(UUID id) {
        rosterRepository.deleteById(id);
    }

    public Page<Roster> getPaginatedEntities(Pageable pageable) {
        return rosterRepository.findAll(pageable);
    }

}
