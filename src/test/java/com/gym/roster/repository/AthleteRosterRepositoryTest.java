package com.gym.roster.repository;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.Country;
import com.doubletuck.gym.common.model.State;
import com.gym.roster.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AthleteRosterRepositoryTest {

    @Autowired
    private AthleteRosterRepository athleteRosterRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    private College testCollege;
    private Athlete testAthlete;
    private AthleteRoster testAthleteRoster;

    @BeforeEach
    void setUp() {
        // Create a test college
        testCollege = new College();
        testCollege.setCodeName("UCLA");
        testCollege.setShortName("UCLA");
        testCollege.setLongName("University of California at Los Angeles");
        testCollege.setCity("Los Angeles");
        testCollege.setState(State.CA.toString());
        testCollege.setConference(Conference.BIGTEN);
        testCollege.setDivision(Division.DIV1);
        testCollege.setRegion(Region.W);
        testCollege.setNickname("Bruins");
        testCollege = collegeRepository.save(testCollege);

        // Create a test athlete
        testAthlete = new Athlete();
        testAthlete.setFirstName("Emily");
        testAthlete.setLastName("Lee");
        testAthlete.setHomeCity("Los Angeles");
        testAthlete.setHomeState(State.CA);
        testAthlete.setHomeCountry(Country.USA);
        testAthlete = athleteRepository.save(testAthlete);

        // Create a test athlete roster
        testAthleteRoster = new AthleteRoster();
        testAthleteRoster.setSeasonYear((short) 2025);
        testAthleteRoster.setCollege(testCollege);
        testAthleteRoster.setAthlete(testAthlete);
        testAthleteRoster.setAcademicYear(AcademicYear.SR);
        testAthleteRoster = athleteRosterRepository.save(testAthleteRoster);
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_WithExactMatch() {
        // When: Querying with exact case match
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UCLA", "Emily", "Lee");

        // Then: The athlete roster should be found
        assertNotNull(found, "AthleteRoster should be found with exact case match");
        assertEquals(testAthleteRoster.getId(), found.getId());
        assertEquals((short) 2025, found.getSeasonYear());
        assertEquals("UCLA", found.getCollege().getCodeName());
        assertEquals("Emily", found.getAthlete().getFirstName());
        assertEquals("Lee", found.getAthlete().getLastName());
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_WithLowerCaseInput() {
        // When: Querying with lowercase input
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "ucla", "emily", "lee");

        // Then: The athlete roster should be found (case-insensitive)
        assertNotNull(found, "AthleteRoster should be found with lowercase input");
        assertEquals(testAthleteRoster.getId(), found.getId());
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_WithUpperCaseInput() {
        // When: Querying with uppercase input
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UCLA", "EMILY", "LEE");

        // Then: The athlete roster should be found (case-insensitive)
        assertNotNull(found, "AthleteRoster should be found with uppercase input");
        assertEquals(testAthleteRoster.getId(), found.getId());
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_WithMixedCaseInput() {
        // When: Querying with mixed case input
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UcLa", "EmIly", "LeE");

        // Then: The athlete roster should be found (case-insensitive)
        assertNotNull(found, "AthleteRoster should be found with mixed case input");
        assertEquals(testAthleteRoster.getId(), found.getId());
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_NullCollegeName() {
        // When: Querying with null college name
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, null, "Emily", "Lee");

        // Then: Should not find match and not fail with null input
        assertNull(found, "Should not return result and no error");
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_NullFirstName() {
        // When: Querying with null college name
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UCLA", null, "Lee");

        // Then: Should not find match and not fail with null input
        assertNull(found, "Should not return result and no error");
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_NullLastName() {
        // When: Querying with null college name
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UCLA", "Emily", null);

        // Then: Should not find match and not fail with null input
        assertNull(found, "Should not return result and no error");
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_WithShortNameMatch() {
        // Given: A college with different codeName and shortName
        College testCollegeAlt = new College();
        testCollegeAlt.setCodeName("UMDCOLLEGEPARK");
        testCollegeAlt.setShortName("Maryland");
        testCollegeAlt.setLongName("University of Maryland at College Park");
        testCollegeAlt.setCity("College Park");
        testCollegeAlt.setState(State.MD.toString());
        testCollegeAlt.setConference(Conference.BIGTEN);
        testCollegeAlt.setDivision(Division.DIV1);
        testCollegeAlt.setRegion(Region.SE);
        testCollegeAlt.setNickname("Terps");
        testCollegeAlt = collegeRepository.save(testCollegeAlt);

        Athlete athlete2 = new Athlete();
        athlete2.setFirstName("Sarah");
        athlete2.setLastName("Johnson");
        athlete2.setHomeCity("Kansas City");
        athlete2.setHomeState(State.MO);
        athlete2 = athleteRepository.save(athlete2);

        AthleteRoster roster2 = new AthleteRoster();
        roster2.setSeasonYear((short) 2025);
        roster2.setCollege(testCollegeAlt);
        roster2.setAthlete(athlete2);
        roster2.setAcademicYear(AcademicYear.JR);
        athleteRosterRepository.save(roster2);

        // When: Querying with short name
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "Maryland", "Sarah", "Johnson");

        // Then: The athlete roster should be found
        assertNotNull(found, "AthleteRoster should be found by shortName");
        assertEquals("UMDCOLLEGEPARK", found.getCollege().getCodeName());
        assertEquals("Maryland", found.getCollege().getShortName());
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_NotFoundWrongYear() {
        // When: Querying with wrong year
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2024, "UCLA", "Emily", "Lee");

        // Then: No athlete roster should be found
        assertNull(found, "AthleteRoster should not be found with wrong year");
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_NotFoundWrongCollege() {
        // When: Querying with wrong college
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "Stanford", "Emily", "Lee");

        // Then: No athlete roster should be found
        assertNull(found, "AthleteRoster should not be found with wrong college");
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_NotFoundWrongFirstName() {
        // When: Querying with wrong first name
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UCLA", "Jessica", "Lee");

        // Then: No athlete roster should be found
        assertNull(found, "AthleteRoster should not be found with wrong first name");
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_NotFoundWrongLastName() {
        // When: Querying with wrong last name
        AthleteRoster found = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UCLA", "Emily", "Smith");

        // Then: No athlete roster should be found
        assertNull(found, "AthleteRoster should not be found with wrong last name");
    }

    @Test
    void testFindBySeasonYearCollegeNameAndAthleteNames_MultipleRostersForDifferentYears() {
        // Given: Another athlete roster for a different year
        AthleteRoster rosterNext = new AthleteRoster();
        rosterNext.setSeasonYear((short) 2026);
        rosterNext.setCollege(testCollege);
        rosterNext.setAthlete(testAthlete);
        rosterNext.setAcademicYear(AcademicYear.GR);
        athleteRosterRepository.save(rosterNext);

        // When: Querying for 2025
        AthleteRoster found2025 = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2025, "UCLA", "Emily", "Lee");

        // When: Querying for 2026
        AthleteRoster found2026 = athleteRosterRepository.findByYearCollegeNameAndAthleteName(
                (short) 2026, "UCLA", "Emily", "Lee");

        // Then: Each should find the correct roster
        assertNotNull(found2025, "Should find 2025 roster");
        assertEquals((short) 2025, found2025.getSeasonYear());
        assertNotNull(found2026, "Should find 2026 roster");
        assertEquals((short) 2026, found2026.getSeasonYear());
        assertNotEquals(found2025.getId(), found2026.getId());
    }

}
