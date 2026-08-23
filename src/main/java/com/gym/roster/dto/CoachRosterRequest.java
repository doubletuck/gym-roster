package com.gym.roster.dto;

import jakarta.validation.constraints.NotNull;

public record CoachRosterRequest(
        @NotNull(message = "College is required")
        Long collegeId,

        @NotNull(message = "Coach is required")
        Long coachId,

        @NotNull(message = "Season year is required")
        Short seasonYear,

        @NotNull(message = "Role is required")
        String roleCode) {
}
