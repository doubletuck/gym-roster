package com.gym.roster.dto;

import com.doubletuck.gym.common.model.AcademicYear;
import com.doubletuck.gym.common.model.StaffRole;
import com.gym.roster.domain.AthleteRoster;
import com.gym.roster.domain.CoachRoster;
import com.gym.roster.domain.College;

import java.util.List;

public record CollegeRosterResponse(
        Long collegeId,
        String collegeCodeName,
        String collegeShortName,
        String collegeLongName,
        Short seasonYear,
        List<AthleteEntry> athletes,
        List<CoachEntry> coaches) {

    public record AthleteEntry(
            Long athleteRosterId,
            Long athleteId,
            String firstName,
            String lastName,
            AcademicYear academicYear,
            String events) {
        public static AthleteEntry from(AthleteRoster roster) {
            return new AthleteEntry(
                    roster.getId(),
                    roster.getAthlete().getId(),
                    roster.getAthlete().getFirstName(),
                    roster.getAthlete().getLastName(),
                    roster.getAcademicYear(),
                    roster.getEvents());
        }
    }

    public record CoachEntry(
            Long coachRosterId,
            Long coachId,
            String firstName,
            String lastName,
            StaffRole roleCode) {
        public static CoachEntry from(CoachRoster roster) {
            return new CoachEntry(
                    roster.getId(),
                    roster.getCoach().getId(),
                    roster.getCoach().getFirstName(),
                    roster.getCoach().getLastName(),
                    roster.getRoleCode());
        }
    }

    public static CollegeRosterResponse from(College college, Short seasonYear,
            List<AthleteRoster> athleteRosters, List<CoachRoster> coachRosters) {
        return new CollegeRosterResponse(
                college.getId(),
                college.getCodeName(),
                college.getShortName(),
                college.getLongName(),
                seasonYear,
                athleteRosters.stream().map(AthleteEntry::from).toList(),
                coachRosters.stream().map(CoachEntry::from).toList());
    }
}
