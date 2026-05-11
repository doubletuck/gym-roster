package com.gym.roster.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AthleteRosterRequest(
        @NotNull(message = "College is required")
        Long collegeId,

        @NotNull(message = "Athlete is required")
        Long athleteId,

        @NotNull(message = "Season year is required")
        Short seasonYear,

        @NotNull(message = "Academic year is required")
        String academicYear,

        @Size(max = 20, message = "Events must have 20 or fewer characters")
        String events) {
}
