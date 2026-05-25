package com.gym.roster.repository;

import com.doubletuck.gym.common.model.StaffRole;
import com.doubletuck.gym.common.model.State;
import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.domain.Conference;
import com.gym.roster.domain.Division;
import com.gym.roster.domain.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CoachRosterRepositoryTest {

    @Autowired
    private CoachRosterRepository coachRosterRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    private College ucla;
    private College utah;
    private Coach alex;
    private Coach jordan;
    private CoachRoster rosterAlexUcla;
    private CoachRoster rosterJordanUtah;

    @BeforeEach
    void setUp() {
        ucla = savedCollege("UCLA", "UCLA", "University of California, Los Angeles", "Los Angeles", State.CA);
        utah = savedCollege("UTAH", "Utah", "University of Utah", "Salt Lake City", State.UT);
        alex = savedCoach("Alex", "Rivera");
        jordan = savedCoach("Jordan", "Smith");
        rosterAlexUcla = savedRoster(alex, ucla, (short) 2024, StaffRole.HEAD_COACH);
        rosterJordanUtah = savedRoster(jordan, utah, (short) 2025, StaffRole.ASST_COACH);
    }

    // --- findByCoachIn ---

    @Test
    void testFindByCoachIn_ReturnsRostersForAllGivenCoaches() {
        // When: Querying with both coaches
        List<CoachRoster> found = coachRosterRepository.findByCoachIn(List.of(alex, jordan));

        // Then: Both rosters are returned
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(r -> r.getId().equals(rosterAlexUcla.getId())));
        assertTrue(found.stream().anyMatch(r -> r.getId().equals(rosterJordanUtah.getId())));
    }

    @Test
    void testFindByCoachIn_ExcludesRostersForOtherCoaches() {
        // When: Querying with only one coach
        List<CoachRoster> found = coachRosterRepository.findByCoachIn(List.of(alex));

        // Then: Only that coach's roster is returned
        assertEquals(1, found.size());
        assertEquals(rosterAlexUcla.getId(), found.get(0).getId());
    }

    @Test
    void testFindByCoachIn_EmptyList_ReturnsEmpty() {
        // When: Querying with an empty list
        List<CoachRoster> found = coachRosterRepository.findByCoachIn(List.of());

        // Then: No results
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByCoachIn_CoachWithMultipleRosters_ReturnsAll() {
        // Given: Alex has a second roster entry
        CoachRoster rosterAlexUtah = savedRoster(alex, utah, (short) 2025, StaffRole.ASST_COACH);

        // When: Querying with Alex
        List<CoachRoster> found = coachRosterRepository.findByCoachIn(List.of(alex));

        // Then: Both of Alex's rosters are returned
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(r -> r.getId().equals(rosterAlexUcla.getId())));
        assertTrue(found.stream().anyMatch(r -> r.getId().equals(rosterAlexUtah.getId())));
    }

    // --- findByYearAndCollegeCodeName ---

    @Test
    void testFindByYearAndCollegeCodeName_ExactMatch() {
        // When: Querying with exact year and college code
        List<CoachRoster> found = coachRosterRepository.findByYearAndCollegeCodeName((short) 2024, "UCLA");

        // Then: Alex's roster is found
        assertEquals(1, found.size());
        assertEquals(rosterAlexUcla.getId(), found.get(0).getId());
    }

    @Test
    void testFindByYearAndCollegeCodeName_CaseInsensitive() {
        // When: Querying with lowercase college code
        List<CoachRoster> found = coachRosterRepository.findByYearAndCollegeCodeName((short) 2024, "ucla");

        // Then: Alex's roster is still found
        assertEquals(1, found.size());
        assertEquals(rosterAlexUcla.getId(), found.get(0).getId());
    }

    @Test
    void testFindByYearAndCollegeCodeName_WrongYear_ReturnsEmpty() {
        // When: Querying with a year that has no rosters for that college
        List<CoachRoster> found = coachRosterRepository.findByYearAndCollegeCodeName((short) 2023, "UCLA");

        // Then: No results
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByYearAndCollegeCodeName_WrongCollege_ReturnsEmpty() {
        // When: Querying with correct year but wrong college
        List<CoachRoster> found = coachRosterRepository.findByYearAndCollegeCodeName((short) 2024, "UTAH");

        // Then: No results (Jordan is at Utah but in 2025, not 2024)
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByYearAndCollegeCodeName_MultipleCoachesAtSameCollegeAndYear() {
        // Given: Jordan also has a roster at UCLA in 2024
        savedRoster(jordan, ucla, (short) 2024, StaffRole.ASST_COACH);

        // When: Querying for that year/college
        List<CoachRoster> found = coachRosterRepository.findByYearAndCollegeCodeName((short) 2024, "UCLA");

        // Then: Both rosters are returned
        assertEquals(2, found.size());
    }

    // --- deleteByYearAndCollegeCodeName ---

    @Test
    void testDeleteByYearAndCollegeCodeName_RemovesMatchingRosters() {
        // When: Deleting UCLA rosters for 2024
        coachRosterRepository.deleteByYearAndCollegeCodeName((short) 2024, "UCLA");

        // Then: Alex's roster is gone
        assertFalse(coachRosterRepository.existsById(rosterAlexUcla.getId()));
    }

    @Test
    void testDeleteByYearAndCollegeCodeName_DoesNotRemoveOtherRosters() {
        // When: Deleting UCLA rosters for 2024
        coachRosterRepository.deleteByYearAndCollegeCodeName((short) 2024, "UCLA");

        // Then: Jordan's unrelated roster is intact
        assertTrue(coachRosterRepository.existsById(rosterJordanUtah.getId()));
    }

    @Test
    void testDeleteByYearAndCollegeCodeName_CaseInsensitive() {
        // When: Deleting with lowercase college code
        coachRosterRepository.deleteByYearAndCollegeCodeName((short) 2024, "ucla");

        // Then: Alex's roster is gone
        assertFalse(coachRosterRepository.existsById(rosterAlexUcla.getId()));
    }

    // --- helpers ---

    private College savedCollege(String codeName, String shortName, String longName, String city, State state) {
        College c = new College();
        c.setCodeName(codeName);
        c.setShortName(shortName);
        c.setLongName(longName);
        c.setCity(city);
        c.setState(state.name());
        c.setConference(Conference.BIGTEN);
        c.setDivision(Division.DIV1);
        c.setRegion(Region.W);
        return collegeRepository.save(c);
    }

    private Coach savedCoach(String firstName, String lastName) {
        Coach c = new Coach();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        return coachRepository.save(c);
    }

    private CoachRoster savedRoster(Coach coach, College college, short seasonYear, StaffRole role) {
        CoachRoster r = new CoachRoster();
        r.setCoach(coach);
        r.setCollege(college);
        r.setSeasonYear(seasonYear);
        r.setRoleCode(role);
        return coachRosterRepository.save(r);
    }
}
