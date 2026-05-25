package com.gym.roster.specification;

import com.doubletuck.gym.common.model.StaffRole;
import com.doubletuck.gym.common.model.State;
import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.domain.Conference;
import com.gym.roster.domain.Division;
import com.gym.roster.domain.Region;
import com.gym.roster.dto.CoachFilterParams;
import com.gym.roster.repository.CoachRepository;
import com.gym.roster.repository.CoachRosterRepository;
import com.gym.roster.repository.CollegeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CoachSpecificationTest {

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private CoachRosterRepository coachRosterRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    // Coaches: alexRivera (UCLA 2024), jordanSmith (Utah 2025), caseyJohnson (no roster)
    private Coach alexRivera;
    private Coach jordanSmith;
    private Coach caseyJohnson;

    @BeforeEach
    void setUp() {
        College ucla = savedCollege("UCLA", "UCLA", "University of California, Los Angeles", "Los Angeles", State.CA);
        College utah = savedCollege("UTAH", "Utah", "University of Utah", "Salt Lake City", State.UT);

        alexRivera = savedCoach("Alex", "Rivera");
        jordanSmith = savedCoach("Jordan", "Smith");
        caseyJohnson = savedCoach("Casey", "Johnson");

        savedRoster(alexRivera, ucla, (short) 2024, StaffRole.HEAD_COACH);
        savedRoster(jordanSmith, utah, (short) 2025, StaffRole.ASST_COACH);
    }

    // --- no params ---

    @Test
    void testNoParams_ReturnsAllCoaches() {
        // When: No filter params are set
        Page<Coach> result = search(null, null, null, null, null);

        // Then: All three coaches are returned
        assertEquals(3, result.getTotalElements());
    }

    // --- firstName ---

    @Test
    void testFirstName_PartialMatch() {
        // When: Filtering by a partial first name
        Page<Coach> result = search(null, "ale", null, null, null);

        // Then: Only Alex is returned
        assertSingleResult(result, alexRivera);
    }

    @Test
    void testFirstName_CaseInsensitive() {
        // When: Filtering with uppercase first name
        Page<Coach> result = search(null, "ALEX", null, null, null);

        // Then: Alex is still found
        assertSingleResult(result, alexRivera);
    }

    @Test
    void testFirstName_NoMatch_ReturnsEmpty() {
        // When: Filtering with a first name that matches nobody
        Page<Coach> result = search(null, "xyz", null, null, null);

        // Then: No results
        assertEquals(0, result.getTotalElements());
    }

    // --- lastName ---

    @Test
    void testLastName_PartialMatch() {
        // When: Filtering by a partial last name
        Page<Coach> result = search(null, null, "smi", null, null);

        // Then: Only Jordan Smith is returned
        assertSingleResult(result, jordanSmith);
    }

    @Test
    void testLastName_CaseInsensitive() {
        // When: Filtering with uppercase last name
        Page<Coach> result = search(null, null, "SMITH", null, null);

        // Then: Jordan Smith is still found
        assertSingleResult(result, jordanSmith);
    }

    // --- q ---

    @Test
    void testQ_MatchesFirstName() {
        // When: q matches Alex's first name
        Page<Coach> result = search("alex", null, null, null, null);

        // Then: Only Alex is returned
        assertSingleResult(result, alexRivera);
    }

    @Test
    void testQ_MatchesLastName() {
        // When: q matches Jordan's last name
        Page<Coach> result = search("smith", null, null, null, null);

        // Then: Only Jordan is returned
        assertSingleResult(result, jordanSmith);
    }

    @Test
    void testQ_MatchesCollegeShortName() {
        // When: q matches the short name of Utah (Jordan's college)
        Page<Coach> result = search("utah", null, null, null, null);

        // Then: Jordan is returned (has a roster at Utah)
        assertSingleResult(result, jordanSmith);
    }

    @Test
    void testQ_MatchesCollegeLongName() {
        // When: q matches part of UCLA's long name
        Page<Coach> result = search("university of california", null, null, null, null);

        // Then: Alex is returned (has a roster at UCLA)
        assertSingleResult(result, alexRivera);
    }

    @Test
    void testQ_CoachWithNoRoster_NotMatchedOnCollegeName() {
        // When: q matches a college name but the coach has no roster
        Page<Coach> result = search("ucla", null, null, null, null);

        // Then: Casey (no roster) is not returned; only Alex is
        assertSingleResult(result, alexRivera);
    }

    @Test
    void testQ_NoMatch_ReturnsEmpty() {
        // When: q matches nothing
        Page<Coach> result = search("zzznomatch", null, null, null, null);

        // Then: No results
        assertEquals(0, result.getTotalElements());
    }

    // --- collegeCodeName ---

    @Test
    void testCollegeCodeName_Filter() {
        // When: Filtering by UCLA
        Page<Coach> result = search(null, null, null, "UCLA", null);

        // Then: Only Alex is returned
        assertSingleResult(result, alexRivera);
    }

    @Test
    void testCollegeCodeName_ExcludesCoachWithNoRoster() {
        // When: Filtering by any college
        Page<Coach> result = search(null, null, null, "UTAH", null);

        // Then: Casey (no roster) is not returned
        List<Long> ids = result.getContent().stream().map(Coach::getId).toList();
        assertFalse(ids.contains(caseyJohnson.getId()));
    }

    @Test
    void testCollegeCodeName_NoMatch_ReturnsEmpty() {
        // When: Filtering by a college that no coach is rostered at
        Page<Coach> result = search(null, null, null, "STANFORD", null);

        // Then: No results
        assertEquals(0, result.getTotalElements());
    }

    // --- seasonYear ---

    @Test
    void testSeasonYear_Filter() {
        // When: Filtering by 2025
        Page<Coach> result = search(null, null, null, null, (short) 2025);

        // Then: Only Jordan is returned
        assertSingleResult(result, jordanSmith);
    }

    @Test
    void testSeasonYear_NoMatch_ReturnsEmpty() {
        // When: Filtering by a year with no rosters
        Page<Coach> result = search(null, null, null, null, (short) 2000);

        // Then: No results
        assertEquals(0, result.getTotalElements());
    }

    // --- collegeCodeName + seasonYear combined ---

    @Test
    void testCollegeCodeNameAndSeasonYear_BothMatch() {
        // When: Filtering by UCLA and 2024 (Alex's exact entry)
        Page<Coach> result = search(null, null, null, "UCLA", (short) 2024);

        // Then: Only Alex is returned
        assertSingleResult(result, alexRivera);
    }

    @Test
    void testCollegeCodeNameAndSeasonYear_YearMismatch_ReturnsEmpty() {
        // When: Filtering by UCLA but the wrong year
        Page<Coach> result = search(null, null, null, "UCLA", (short) 2025);

        // Then: No results (Alex is at UCLA but in 2024, not 2025)
        assertEquals(0, result.getTotalElements());
    }

    // --- helpers ---

    private Page<Coach> search(String q, String firstName, String lastName,
            String collegeCodeName, Short seasonYear) {
        CoachFilterParams params = new CoachFilterParams(q, firstName, lastName, collegeCodeName, seasonYear);
        return coachRepository.findAll(CoachSpecification.build(params), PageRequest.of(0, 10));
    }

    private void assertSingleResult(Page<Coach> result, Coach expected) {
        assertEquals(1, result.getTotalElements());
        assertEquals(expected.getId(), result.getContent().get(0).getId());
    }

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
