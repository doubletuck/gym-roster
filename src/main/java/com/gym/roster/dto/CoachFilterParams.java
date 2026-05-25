package com.gym.roster.dto;

public record CoachFilterParams(
        String q,
        String firstName,
        String lastName,
        String collegeCodeName,
        Short seasonYear) {
}