package com.gym.roster.domain;

import com.doubletuck.gym.common.model.StaffRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "coach_roster", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_coach_roster",
                columnNames = {"season_year", "college_id", "coach_id"})
})
public class CoachRoster extends BaseEntity {

    @NotNull(message = "College is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "college_id", nullable = false, foreignKey = @ForeignKey(name = "fk_coach_roster_college"))
    private College college;

    @NotNull(message = "Season year is required")
    @Column(name = "season_year", nullable = false)
    private Short seasonYear;

    @NotNull(message = "Coach is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coach_id", nullable = false, foreignKey = @ForeignKey(name = "fk_coach_roster_coach"))
    private Coach coach;

    @NotNull(message = "Role code is required")
    @Column(name = "role_code", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StaffRole roleCode;
}
