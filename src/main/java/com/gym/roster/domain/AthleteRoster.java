package com.gym.roster.domain;

import com.doubletuck.gym.common.model.AcademicYear;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * Represents a college women's gymnastics team roster for a given season.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "athlete_roster", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_athlete_roster",
                columnNames = {"season_year", "college_id", "athlete_id"})
})
public class AthleteRoster extends BaseEntity {

    @NotNull(message = "College is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "college_id", nullable = false, foreignKey = @ForeignKey(name = "fk_athlete_roster_college"))
    private College college;

    @NotNull(message = "Season year is required")
    @Column(name = "season_year", nullable = false)
    private Short seasonYear;

    @NotNull(message = "Athlete is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "athlete_id", nullable = false, foreignKey = @ForeignKey(name = "fk_athlete_roster_athlete"))
    private Athlete athlete;

    @NotNull(message = "Academic year is required")
    @Column(name = "academic_year", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AcademicYear academicYear;

    @Column(name = "event", length = 20)
    private String event;

    public void setEvent(String position) {
        this.event = (position == null || position.isBlank()) ? null : position.trim();
    }
}
