package com.gym.roster.domain;

import com.doubletuck.gym.common.model.Event;
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

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "meet_score", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_meet_score",
                columnNames = { "athlete_roster_id", "event_code" })
})
public class MeetScore extends BaseEntity {

    @NotNull(message = "Athlete is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "athlete_roster_id", nullable = false, foreignKey = @ForeignKey(name = "fk_meet_score_athlete_roster"))
    private AthleteRoster athleteRoster;

    @NotNull(message = "Event is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "event_code", nullable = false, length = 20)
    private Event event;

    @Column(name = "round")
    private Integer round;

    @Column(name = "order")
    private Integer order;

    @NotNull(message = "Score is required")
    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "difficulty_score")
    private Double difficultyScore;

    @Column(name = "execution_score")
    private Double executionScore;

    @Column(name = "neutral_deduction")
    private Double neutralDeduction;

    @Column(name = "bonus_points")
    private Double bonusPoints;

    @Column(name = "has_score_inquiry", nullable = false)
    private Boolean hasScoreInquiry = false;

    @Column(name = "is_score_edited", nullable = false)
    private Boolean isScoreEdited = false;

    private List<MeetScoreDetail> scoreDetails;
}

class MeetScoreDetail {
    private String judge;
    private Double startValue;
    private Double score;
}
