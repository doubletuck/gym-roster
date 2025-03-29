package com.gym.roster.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "college")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "long_name", nullable = false)
    private String longName;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state_code", nullable = false)
    private String stateCode;

    @Column(name = "conference", nullable = false)
    private Conference conference;

    @Column(name = "division", nullable = false)
    private Division division;

    @Column(name = "region", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "college", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CollegeConferenceHistory> conferenceHistoryList;

    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private Instant creationTimestamp;

    @Column(name = "last_update_timestamp", nullable = false)
    private Instant lastUpdateTimestamp;
}
