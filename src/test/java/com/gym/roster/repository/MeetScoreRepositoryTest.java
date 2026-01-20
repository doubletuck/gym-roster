package com.gym.roster.repository;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.Country;
import com.doubletuck.gym.common.model.Event;
import com.doubletuck.gym.common.model.State;
import com.gym.roster.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class MeetScoreRepositoryTest {

    @Autowired
    private MeetScoreRepository meetScoreRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private AthleteRosterRepository athleteRosterRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    private Meet testMeet;
    private AthleteRoster testAthleteRoster;
    private MeetScore testMeetScore;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @BeforeEach
    void setUp() {
        // Create a test college
        College testCollege = new College();
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
        Athlete testAthlete = new Athlete();
        testAthlete.setFirstName("Jessica");
        testAthlete.setLastName("Smith");
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

        // Create a test meet
        testMeet = new Meet();
        testMeet.setEventDate(LocalDate.parse("01/10/2026", dateFormatter));
        testMeet.setEventName("Regional Meet");
        testMeet.setLocation("Los Angeles");
        testMeet = meetRepository.save(testMeet);

        // Create a test meet score
        testMeetScore = new MeetScore();
        testMeetScore.setMeet(testMeet);
        testMeetScore.setAthleteRoster(testAthleteRoster);
        testMeetScore.setEvent(Event.VT);
        testMeetScore.setRotation(1);
        testMeetScore.setStartOrder(3);
        testMeetScore.setFinalScore(14.5);
        testMeetScore.setDifficultyScore(5.0);
        testMeetScore.setExecutionScore(9.5);
        testMeetScore.setNeutralDeduction(0.0);
        testMeetScore.setBonusPoints(0.0);
        testMeetScore.setHasScoreInquiry(false);
        testMeetScore.setIsScoreEdited(false);
        testMeetScore.addScoreDetail(new MeetScoreDetail("1", 10.0, 9.85));
        testMeetScore.addScoreDetail(new MeetScoreDetail("2", 10.0, 9.8));
        testMeetScore = meetScoreRepository.save(testMeetScore);
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_WithExactMatch() {
        // When: Querying with exact match parameters
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, testAthleteRoster, Event.VT);

        // Then: The meet score should be found with all fields intact
        assertNotNull(found, "MeetScore should be found with exact parameters");
        assertEquals(testMeetScore.getId(), found.getId());
        assertEquals(testMeet.getId(), found.getMeet().getId());
        assertEquals(testAthleteRoster.getId(), found.getAthleteRoster().getId());
        assertEquals(Event.VT, found.getEvent());
        assertEquals(14.5, found.getFinalScore());
        assertEquals(5.0, found.getDifficultyScore());
        assertEquals(9.5, found.getExecutionScore());
        assertEquals(1, found.getRotation());
        assertEquals(3, found.getStartOrder());
        assertEquals(2, found.getScoreDetails().size());
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_WrongEvent() {
        // When: Querying with wrong event
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, testAthleteRoster, Event.UB);

        // Then: Should not find any score
        assertNull(found, "MeetScore should not be found with wrong event");
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_WrongMeet() {
        // Given: Another meet is created
        Meet anotherMeet = new Meet();
        anotherMeet.setEventDate(LocalDate.parse("02/01/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        anotherMeet.setEventName("State Championships");
        anotherMeet.setLocation("San Francisco");
        anotherMeet = meetRepository.save(anotherMeet);

        // When: Querying with different meet
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                anotherMeet, testAthleteRoster, Event.VT);

        // Then: Should not find any score
        assertNull(found, "MeetScore should not be found with wrong meet");
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_WrongAthleteRoster() {
        // Given: Another athlete is created
        Athlete anotherAthlete = new Athlete();
        anotherAthlete.setFirstName("Emily");
        anotherAthlete.setLastName("Johnson");
        anotherAthlete.setHomeCity("San Diego");
        anotherAthlete.setHomeState(State.CA);
        anotherAthlete.setHomeCountry(Country.USA);
        anotherAthlete = athleteRepository.save(anotherAthlete);

        // Get the test college for roster
        College testCollege = testAthleteRoster.getCollege();

        // And another athlete roster is created
        AthleteRoster anotherRoster = new AthleteRoster();
        anotherRoster.setSeasonYear((short) 2025);
        anotherRoster.setCollege(testCollege);
        anotherRoster.setAthlete(anotherAthlete);
        anotherRoster.setAcademicYear(AcademicYear.JR);
        anotherRoster = athleteRosterRepository.save(anotherRoster);

        // When: Querying with different athlete roster
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, anotherRoster, Event.VT);

        // Then: Should not find any score
        assertNull(found, "MeetScore should not be found with wrong athlete roster");
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_AllParametersWrong() {
        // Given: Another meet, athlete, and college
        Meet anotherMeet = new Meet();
        anotherMeet.setEventDate(LocalDate.parse("03/15/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        anotherMeet.setEventName("Invitational");
        anotherMeet = meetRepository.save(anotherMeet);

        College anotherCollege = new College();
        anotherCollege.setCodeName("STANF");
        anotherCollege.setShortName("Stanford");
        anotherCollege.setLongName("Stanford University");
        anotherCollege.setCity("Stanford");
        anotherCollege.setState(State.CA.toString());
        anotherCollege.setConference(Conference.PAC12);
        anotherCollege.setDivision(Division.DIV1);
        anotherCollege.setRegion(Region.W);
        anotherCollege.setNickname("Cardinal");
        anotherCollege = collegeRepository.save(anotherCollege);

        Athlete anotherAthlete = new Athlete();
        anotherAthlete.setFirstName("Rachel");
        anotherAthlete.setLastName("Davis");
        anotherAthlete.setHomeCity("Palo Alto");
        anotherAthlete.setHomeState(State.CA);
        anotherAthlete.setHomeCountry(Country.USA);
        anotherAthlete = athleteRepository.save(anotherAthlete);

        AthleteRoster anotherRoster = new AthleteRoster();
        anotherRoster.setSeasonYear((short) 2025);
        anotherRoster.setCollege(anotherCollege);
        anotherRoster.setAthlete(anotherAthlete);
        anotherRoster.setAcademicYear(AcademicYear.SR);
        anotherRoster = athleteRosterRepository.save(anotherRoster);

        // When: Querying with all different parameters
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                anotherMeet, anotherRoster, Event.BB);

        // Then: Should not find any score
        assertNull(found, "MeetScore should not be found when all parameters are wrong");
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_MultipleScoresForSameAthleteAndMeet() {
        // Given: Another meet score with same meet and athlete but different event
        MeetScore anotherScore = new MeetScore();
        anotherScore.setMeet(testMeet);
        anotherScore.setAthleteRoster(testAthleteRoster);
        anotherScore.setEvent(Event.UB);
        anotherScore.setFinalScore(13.75);
        anotherScore.setDifficultyScore(4.5);
        anotherScore.setExecutionScore(9.25);
        anotherScore = meetScoreRepository.save(anotherScore);

        // When: Querying for VAULT
        MeetScore foundVault = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, testAthleteRoster, Event.VT);

        // And: Querying for BARS
        MeetScore foundBars = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, testAthleteRoster, Event.UB);

        // Then: Each query should return the correct score
        assertNotNull(foundVault, "VAULT score should be found");
        assertEquals(Event.VT, foundVault.getEvent());
        assertEquals(14.5, foundVault.getFinalScore());
        assertNotNull(foundBars, "BARS score should be found");
        assertEquals(Event.UB, foundBars.getEvent());
        assertEquals(13.75, foundBars.getFinalScore());
        assertNotEquals(foundVault.getId(), foundBars.getId());
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_MultipleScoresAcrossDifferentMeets() {
        // Given: Another meet
        Meet anotherMeet = new Meet();
        anotherMeet.setEventDate(LocalDate.parse("02/10/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        anotherMeet.setEventName("Conference Championship");
        anotherMeet.setLocation("San Diego");
        anotherMeet = meetRepository.save(anotherMeet);

        // And: A score for the same athlete in the different meet
        MeetScore anotherScore = new MeetScore();
        anotherScore.setMeet(anotherMeet);
        anotherScore.setAthleteRoster(testAthleteRoster);
        anotherScore.setEvent(Event.VT);
        anotherScore.setFinalScore(15.0);
        anotherScore = meetScoreRepository.save(anotherScore);

        // When: Querying for VAULT in the original meet
        MeetScore foundOriginalMeet = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, testAthleteRoster, Event.VT);

        // And: Querying for VAULT in the different meet
        MeetScore foundAnotherMeet = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                anotherMeet, testAthleteRoster, Event.VT);

        // Then: Each query should return the correct score for its meet
        assertNotNull(foundOriginalMeet, "Score should be found in original meet");
        assertEquals(testMeet.getId(), foundOriginalMeet.getMeet().getId());
        assertEquals(14.5, foundOriginalMeet.getFinalScore());
        assertNotNull(foundAnotherMeet, "Score should be found in different meet");
        assertEquals(anotherMeet.getId(), foundAnotherMeet.getMeet().getId());
        assertEquals(15.0, foundAnotherMeet.getFinalScore());
        assertNotEquals(foundOriginalMeet.getId(), foundAnotherMeet.getId());
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_AllEventTypes() {
        Meet anotherMeet = new Meet();
        anotherMeet.setEventDate(LocalDate.parse("01/03/2026", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        anotherMeet.setEventName("Sprouts Invitational Session I");
        anotherMeet = meetRepository.save(anotherMeet);

        // Test all enum event types are supported
        Event[] allEvents = Event.values();

        for (Event event : allEvents) {
            // Given: A meet score is created for each event
            MeetScore score = new MeetScore();
            score.setMeet(anotherMeet);
            score.setAthleteRoster(testAthleteRoster);
            score.setEvent(event);
            score.setFinalScore(13.5 + allEvents.length);
            meetScoreRepository.save(score);

            // When: Querying for that event
            MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                    anotherMeet, testAthleteRoster, event);

            // Then: The score should be found
            assertNotNull(found, "MeetScore should be found for event: " + event);
            assertEquals(event, found.getEvent());
        }
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_NullMeet() {
        // When: Querying with null meet
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                null, testAthleteRoster, Event.VT);

        // Then: Should not find any score
        assertNull(found, "MeetScore should not be found with null meet");
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_NullAthleteRoster() {
        // When: Querying with null athlete roster
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, null, Event.VT);

        // Then: Should not find any score
        assertNull(found, "MeetScore should not be found with null athlete roster");
    }

    @Test
    void testFindByMeetAthleteRosterAndEvent_NullEvent() {
        // When: Querying with null event
        MeetScore found = meetScoreRepository.findByMeetAthleteRosterAndEvent(
                testMeet, testAthleteRoster, null);

        // Then: Should not find any score
        assertNull(found, "MeetScore should not be found with null event");
    }
}
