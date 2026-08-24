package com.gym.roster.controller;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.StaffRole;
import tools.jackson.databind.ObjectMapper;
import com.gym.roster.domain.Athlete;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.Coach;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;
import com.gym.roster.dto.AthleteRosterRequest;
import com.gym.roster.dto.CoachRosterRequest;
import com.gym.roster.service.AthleteService;
import com.gym.roster.service.CoachService;
import com.gym.roster.service.CollegeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RosterController.class)
@AutoConfigureMockMvc(addFilters = false)
class RosterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CollegeService collegeService;

    @MockitoBean
    private AthleteService athleteService;

    @MockitoBean
    private CoachService coachService;

    private College ucla() {
        College college = new College();
        college.setId(1L);
        college.setCodeName("UCLA");
        college.setShortName("UCLA");
        college.setLongName("University of California, Los Angeles");
        return college;
    }

    private AthleteRoster athleteRoster() {
        Athlete athlete = new Athlete();
        athlete.setId(101L);
        athlete.setFirstName("Emily");
        athlete.setLastName("Lee");
        athlete.setHomeCity("Los Angeles");

        AthleteRoster roster = new AthleteRoster();
        roster.setId(201L);
        roster.setCollege(ucla());
        roster.setSeasonYear((short) 2024);
        roster.setAthlete(athlete);
        roster.setAcademicYear(AcademicYear.JR);
        return roster;
    }

    private CoachRoster coachRoster() {
        Coach coach = new Coach();
        coach.setId(102L);
        coach.setFirstName("Alex");
        coach.setLastName("Rivera");

        CoachRoster roster = new CoachRoster();
        roster.setId(202L);
        roster.setCollege(ucla());
        roster.setSeasonYear((short) 2024);
        roster.setCoach(coach);
        roster.setRoleCode(StaffRole.HEAD_COACH);
        return roster;
    }

    // --- Combined roster endpoint ---

    @Test
    void getCollegeRosterForSeason_ReturnsCombinedRosterWhenCollegeExists() throws Exception {
        when(collegeService.findByCodeName("UCLA")).thenReturn(ucla());
        when(athleteService.findRosterByYearAndCollegeCode((short) 2024, "UCLA"))
                .thenReturn(List.of(athleteRoster()));
        when(coachService.findRosterByYearAndCollegeCode((short) 2024, "UCLA"))
                .thenReturn(List.of(coachRoster()));

        mockMvc.perform(get("/roster/2024/UCLA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collegeCodeName").value("UCLA"))
                .andExpect(jsonPath("$.seasonYear").value(2024))
                .andExpect(jsonPath("$.athletes[0].lastName").value("Lee"))
                .andExpect(jsonPath("$.coaches[0].lastName").value("Rivera"));
    }

    @Test
    void getCollegeRosterForSeason_ReturnsEmptyListsWhenNoRosterEntries() throws Exception {
        when(collegeService.findByCodeName("UCLA")).thenReturn(ucla());
        when(athleteService.findRosterByYearAndCollegeCode((short) 2024, "UCLA")).thenReturn(List.of());
        when(coachService.findRosterByYearAndCollegeCode((short) 2024, "UCLA")).thenReturn(List.of());

        mockMvc.perform(get("/roster/2024/UCLA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.athletes").isEmpty())
                .andExpect(jsonPath("$.coaches").isEmpty());
    }

    @Test
    void getCollegeRosterForSeason_ReturnsNotFoundWhenCollegeMissing() throws Exception {
        when(collegeService.findByCodeName("NOPE")).thenReturn(null);

        mockMvc.perform(get("/roster/2024/NOPE"))
                .andExpect(status().isNotFound());
    }

    // --- Athlete roster ---

    @Test
    void createAthleteRoster_ReturnsCreatedWhenValid() throws Exception {
        AthleteRosterRequest request = new AthleteRosterRequest(1L, 101L, (short) 2024, "JR", "VAULT");
        when(athleteService.createRoster(any())).thenReturn(Optional.of(athleteRoster()));

        mockMvc.perform(post("/roster/athlete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createAthleteRoster_ReturnsNotFoundWhenReferenceMissing() throws Exception {
        AthleteRosterRequest request = new AthleteRosterRequest(999L, 101L, (short) 2024, "JR", "VAULT");
        when(athleteService.createRoster(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/roster/athlete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCollegeAthleteRosterForSeason_ReturnsAthleteRosterList() throws Exception {
        when(athleteService.findRosterByYearAndCollegeCode((short) 2024, "UCLA"))
                .thenReturn(List.of(athleteRoster()));

        mockMvc.perform(get("/roster/athlete/2024/UCLA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].athlete.lastName").value("Lee"));
    }

    @Test
    void deleteAthleteRosterById_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/roster/athlete/201"))
                .andExpect(status().isNoContent());

        verify(athleteService, times(1)).deleteRosterById(201L);
    }

    @Test
    void importAthleteRosterFromFile_ReturnsOkWithEmptyResultsForHeaderOnlyFile() throws Exception {
        String csv = "COLLEGE_CODE_NAME,YEAR,FIRST_NAME,LAST_NAME,ACADEMIC_YEAR,HOME_TOWN,HOME_STATE,HOME_COUNTRY,CLUB,EVENT\n";
        MockMultipartFile file = new MockMultipartFile("file", "athlete-roster.csv", "text/csv", csv.getBytes());

        mockMvc.perform(multipart("/roster/athlete/file-import").file(file))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void importAthleteRosterFromDirectory_ReturnsOkTrueForEmptyDirectory(@TempDir Path tempDir) throws Exception {
        mockMvc.perform(post("/roster/athlete/directory-import")
                        .param("directoryPath", tempDir.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // --- Coach roster ---

    @Test
    void createCoachRoster_ReturnsCreatedWhenValid() throws Exception {
        CoachRosterRequest request = new CoachRosterRequest(1L, 102L, (short) 2024, "HEAD_COACH");
        when(coachService.createRoster(any())).thenReturn(Optional.of(coachRoster()));

        mockMvc.perform(post("/roster/coach")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createCoachRoster_ReturnsNotFoundWhenReferenceMissing() throws Exception {
        CoachRosterRequest request = new CoachRosterRequest(999L, 102L, (short) 2024, "HEAD_COACH");
        when(coachService.createRoster(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/roster/coach")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCoachRoster_ReturnsBadRequestWhenRoleMissing() throws Exception {
        String invalidJson = "{\"collegeId\":1,\"coachId\":102,\"seasonYear\":2024}";

        mockMvc.perform(post("/roster/coach")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findCoachRosterById_ReturnsCoachRosterWhenFound() throws Exception {
        when(coachService.findRosterById(202L)).thenReturn(Optional.of(coachRoster()));

        mockMvc.perform(get("/roster/coach/202"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coach.lastName").value("Rivera"));
    }

    @Test
    void findCoachRosterById_ReturnsNotFoundWhenMissing() throws Exception {
        when(coachService.findRosterById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/roster/coach/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCollegeCoachRosterForSeason_ReturnsCoachRosterList() throws Exception {
        when(coachService.findRosterByYearAndCollegeCode((short) 2024, "UCLA"))
                .thenReturn(List.of(coachRoster()));

        mockMvc.perform(get("/roster/coach/2024/UCLA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].coach.lastName").value("Rivera"));
    }

    @Test
    void deleteCoachRosterById_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/roster/coach/202"))
                .andExpect(status().isNoContent());

        verify(coachService, times(1)).deleteRosterById(202L);
    }

    @Test
    void deleteCollegeCoachRosterForSeason_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/roster/coach/2024/UCLA"))
                .andExpect(status().isNoContent());

        verify(coachService, times(1)).deleteRosterByYearAndCollegeCodeName((short) 2024, "UCLA");
    }

    @Test
    void importCoachRosterFromFile_ReturnsOkWithEmptyResultsForHeaderOnlyFile() throws Exception {
        String csv = "COLLEGE_CODE_NAME,YEAR,FIRST_NAME,LAST_NAME,ROLE\n";
        MockMultipartFile file = new MockMultipartFile("file", "staff-roster.csv", "text/csv", csv.getBytes());

        mockMvc.perform(multipart("/roster/coach/file-import").file(file))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void importCoachRosterFromDirectory_ReturnsOkTrueForEmptyDirectory(@TempDir Path tempDir) throws Exception {
        mockMvc.perform(post("/roster/coach/directory-import")
                        .param("directoryPath", tempDir.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
