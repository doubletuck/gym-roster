package com.gym.roster.domain;

import com.doubletuck.gym.common.model.Country;
import com.doubletuck.gym.common.model.State;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "athlete", uniqueConstraints = {
        @UniqueConstraint(name = "uk_athlete", columnNames = { "first_name", "last_name", "home_city" })
})
@Relation(collectionRelation = "content")
public class Athlete extends BaseEntity {

    @NotBlank(message = "First name is required")
    @Size(max = 40, message = "First name must have 40 or fewer characters")
    @Column(name = "first_name", nullable = false, length = 40)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 40, message = "Last name must have 40 or fewer characters")
    @Column(name = "last_name", nullable = false, length = 40)
    private String lastName;

    @NotBlank(message = "Home city is required")
    @Size(max = 50, message = "Home city must have 50 or fewer characters")
    @Column(name = "home_city", nullable = false, length = 50)
    private String homeCity;

    @Column(name = "home_state_code", length = 2)
    @Enumerated(EnumType.STRING)
    private State homeState;

    @Column(name = "home_country_code", length = 3)
    @Enumerated(EnumType.STRING)
    private Country homeCountry;

    @Column(name = "club_name", length = 100)
    @Size(max = 100, message = "Club name must have 100 or fewer characters")
    private String clubName;

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = (homeCity == null || homeCity.isBlank()) ? null : homeCity.trim();
    }

    public void setHomeState(State homeState) {
        this.homeState = homeState;
    }

    public void setHomeState(String homeState) {
        if (homeState == null || homeState.trim().isEmpty()) {
            this.homeState = null;
            return;
        }
        this.homeState = State.find(homeState);
        if (this.homeState == null) {
            throw new IllegalArgumentException("State is invalid: " + homeState);
        }
    }

    public void setHomeCountry(Country homeCountry) {
        this.homeCountry = homeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        if (homeCountry == null || homeCountry.trim().isEmpty()) {
            this.homeCountry = null;
            return;
        }
        this.homeCountry = Country.find(homeCountry);
        if (this.homeCountry == null) {
            throw new IllegalArgumentException("Country is invalid: " + homeCountry);
        }
    }

    public void setClubName(String clubName) {
        this.clubName = (clubName == null || clubName.isBlank()) ? null : clubName.trim();
    }
}
