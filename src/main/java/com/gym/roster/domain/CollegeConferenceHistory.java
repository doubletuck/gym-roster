package com.gym.roster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "college_conference_history", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_college_conf_history",
                columnNames = {"college_id", "start_year"})
})
public class CollegeConferenceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Column(name = "start_year", nullable = false)
    private Short startYear;

    @Column(name = "end_year")
    private Short endYear;

    @Column(name = "conference", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Conference conference;

    @Column(name = "division", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Division division;

    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private Instant creationTimestamp;

    @Column(name = "last_update_timestamp", nullable = false)
    private Instant lastUpdateTimestamp;
}
