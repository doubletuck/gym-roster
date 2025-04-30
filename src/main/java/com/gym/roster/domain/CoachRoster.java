package com.gym.roster.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "coach_roster", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_coach_roster",
                columnNames = {"season_year", "college_id", "coach_id"})
})
public class CoachRoster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "College is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @NotNull(message = "Season year is required")
    @Column(name = "season_year", nullable = false)
    private Short seasonYear;

    @NotNull(message = "Coach is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @NotBlank(message = "Role code is required")
    @Column(name = "role_code", nullable = false)
    private String roleCode;

    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private Instant creationTimestamp;

    @Column(name = "last_update_timestamp", nullable = false)
    private Instant lastUpdateTimestamp;

}
