package com.gym.roster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "college", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_college",
                columnNames = {"code_name"})
})
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Code name is required")
    @Column(name = "code_name", nullable = false, length = 20)
    private String codeName;

    @NotBlank(message = "Short name is required")
    @Size(max = 30, message = "Short name must have 30 or fewer characters")
    @Column(name = "short_name", nullable = false, length = 30)
    private String shortName;

    @NotBlank(message = "Long name is required")
    @Size(max = 50, message = "Long name must have 50 or fewer characters")
    @Column(name = "long_name", nullable = false, length = 50)
    private String longName;

    @NotBlank(message = "City is required")
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull(message = "State is required")
    @Column(name = "state_code", nullable = false, length = 2)
    private String state;

    @NotNull(message = "Conference is required")
    @Column(name = "conference", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Conference conference;

    @NotNull(message = "Division is required")
    @Column(name = "division", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Division division;

    @NotNull(message = "Region is required")
    @Column(name = "region", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Size(max = 20, message = "Nickname must have 20 or fewer characters")
    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "team_url")
    private String teamUrl;

    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private Instant creationTimestamp;

    @Column(name = "last_update_timestamp", nullable = false)
    private Instant lastUpdateTimestamp;
}
