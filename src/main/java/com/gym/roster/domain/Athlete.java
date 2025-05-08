package com.gym.roster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "athlete", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_athlete",
                columnNames = {"first_name", "last_name", "home_city"})
})
public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(max = 30, message = "First name must have 30 or fewer characters")
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name must have 30 or fewer characters")
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @NotBlank(message = "Home city is required")
    @Size(max = 30, message = "Home city must have 30 or fewer characters")
    @Column(name = "home_city", nullable = false, length = 30)
    private String homeCity;

    @Column(name = "home_state_code", length = 2)
    private String homeState;

    @Column(name = "home_country_code", length = 3)
    private String homeCountry;

    @Column(name = "club_name", length = 50)
    private String clubName;

    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private Instant creationTimestamp;

    @Column(name = "last_update_timestamp", nullable = false)
    private Instant lastUpdateTimestamp;

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = (homeCity == null || homeCity.isBlank()) ? null : homeCity.trim();
    }

    public void setHomeState(String homeState) {
        this.homeState = (homeState == null || homeState.isBlank()) ? null : homeState.trim();
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = (homeCountry == null || homeCountry.isBlank()) ? null : homeCountry.trim();
    }

    public void setClubName(String clubName) {
        this.clubName = (clubName == null || clubName.isBlank()) ? null : clubName.trim();
    }
}
