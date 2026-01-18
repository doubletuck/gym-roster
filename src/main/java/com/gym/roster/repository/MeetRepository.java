package com.gym.roster.repository;

import com.gym.roster.domain.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long> {

    Meet findByEventDateAndEventNameIgnoreCase(LocalDate eventDate, String eventName);
}
