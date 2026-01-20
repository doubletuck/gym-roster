package com.gym.roster.repository;

import com.gym.roster.domain.Meet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
public class MeetRepositoryTest {

    @Autowired
    private MeetRepository meetRepository;

    private Meet testMeet;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @BeforeEach
    void setUp() {
        // Create a test meet
        testMeet = new Meet();
        testMeet.setEventDate(LocalDate.parse("01/02/2026", dateFormatter));
        testMeet.setEventName("Test Meet");
        testMeet = meetRepository.save(testMeet);
    }

    @Test
    void testFindByEventDateAndEventNameIgnoreCase_DifferentCaseForName() {
        Meet found = meetRepository.findByEventDateAndEventNameIgnoreCase(testMeet.getEventDate(), testMeet.getEventName().toUpperCase());
        assertNotNull(found, "Meet should be found by date and name");
    }

    @Test
    void testFindByEventDateAndEventNameIgnoreCase_WrongDate() {
        Meet found = meetRepository.findByEventDateAndEventNameIgnoreCase(
                LocalDate.parse("04/01/2025", dateFormatter), testMeet.getEventName());
        assertNull(found, "Meet should not be found");
    }

    @Test
    void testFindByEventDateAndEventNameIgnoreCase_WrongName() {
        Meet found = meetRepository.findByEventDateAndEventNameIgnoreCase(testMeet.getEventDate(), "Wrong Name");
        assertNull(found, "Meet should not be found");
    }
}
