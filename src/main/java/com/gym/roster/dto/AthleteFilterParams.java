package com.gym.roster.dto;

public record AthleteFilterParams(
        String q,
        String firstName,
        String lastName,
        String homeCity,
        String homeState,
        String homeCountry,
        String clubName,
        String collegeCodeName,
        Short seasonYear,
        String academicYear
) {}
