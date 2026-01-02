package com.gym.roster.repository;

import com.doubletuck.gym.common.model.State;
import com.gym.roster.domain.College;
import com.gym.roster.domain.Conference;
import com.gym.roster.domain.Division;
import com.gym.roster.domain.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CollegeRepositoryTest {

    @Autowired
    private CollegeRepository collegeRepository;

    private College testCollege;

    @BeforeEach
    void setUp() {
        testCollege = new College();
        testCollege.setCodeName("BGSU");
        testCollege.setShortName("Bowling Green");
        testCollege.setLongName("Bowling Green State University");
        testCollege.setCity("Bowling Green");
        testCollege.setState(State.OH.toString());
        testCollege.setConference(Conference.MAC);
        testCollege.setDivision(Division.DIV2);
        testCollege.setRegion(Region.NA);
        testCollege.setNickname("Falcons");
        testCollege.setTeamUrl("https://www.bgsufalcons.com");
    }

    @Test
    void testSave() {
        // Given: A new college entity
        assertNull(testCollege.getId(), "ID should be null before save");

        // When: Saving the college to the database
        College createdCollege = collegeRepository.save(testCollege);

        // Then: The college should be persisted with an ID
        assertNotNull(createdCollege.getId(), "ID should be generated");
        assertEquals("BGSU", createdCollege.getCodeName(), "Code name");
        assertEquals("Bowling Green", createdCollege.getShortName(), "Short name");
        assertEquals("Bowling Green State University", createdCollege.getLongName(), "Long name");
        assertEquals("Bowling Green", createdCollege.getCity(), "City");
        assertEquals(State.OH, createdCollege.getState(), "State");
        assertEquals(Conference.MAC, createdCollege.getConference(), "Conference");
        assertEquals(Division.DIV2, createdCollege.getDivision(), "Division");
        assertEquals(Region.NA, createdCollege.getRegion(), "Region");
        assertEquals("Falcons", createdCollege.getNickname(), "Nickname");
        assertNotNull(createdCollege.getCreationTimestamp(), "Creation timestamp should be generated");
        assertNotNull(createdCollege.getLastUpdateTimestamp(), "Updated timestamp should be generated");
    }

    @Test
    void testQueryById() {
        // Given: A college is saved in the database
        College savedCollege = collegeRepository.save(testCollege);
        Long savedId = savedCollege.getId();

        // When: Querying by ID
        Optional<College> foundCollege = collegeRepository.findById(savedId);

        // Then: The college should be found with all fields intact
        assertTrue(foundCollege.isPresent(), "College should be found by ID");
        assertEquals("BGSU", foundCollege.get().getCodeName());
        assertEquals("Bowling Green", foundCollege.get().getShortName());
    }

    @Test
    void testQueryByCodeName() {
        // Given: A college is saved in the database
        collegeRepository.save(testCollege);

        // When: Querying by code name using custom query
        College foundCollege = collegeRepository.findByCodeName("BGSU");

        // Then: The college should be found
        assertNotNull(foundCollege, "College should be found by code name");
        assertEquals("BGSU", foundCollege.getCodeName());
        assertEquals("Bowling Green", foundCollege.getShortName());
    }

    @Test
    void testDelete() {
        // Given: A college is saved in the database
        College savedCollege = collegeRepository.save(testCollege);
        Long savedId = savedCollege.getId();
        assertTrue(collegeRepository.existsById(savedId), "College should exist before delete");

        // When: Deleting the college
        collegeRepository.deleteById(savedId);

        // Then: The college should no longer exist in the database
        assertFalse(collegeRepository.existsById(savedId), "College should not exist after delete");
        Optional<College> deletedCollege = collegeRepository.findById(savedId);
        assertFalse(deletedCollege.isPresent(), "College should not be found after delete");
    }
}
