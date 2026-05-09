package com.gym.roster.dto;

import com.doubletuck.gym.common.model.Country;
import com.doubletuck.gym.common.model.State;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AthleteRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 40, message = "First name must have 40 or fewer characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 40, message = "Last name must have 40 or fewer characters")
        String lastName,

        @NotBlank(message = "Home city is required")
        @Size(max = 50, message = "Home city must have 50 or fewer characters")
        String homeCity,

        State homeState,

        Country homeCountry,

        @Size(max = 100, message = "Club name must have 100 or fewer characters")
        String clubName) {
}
