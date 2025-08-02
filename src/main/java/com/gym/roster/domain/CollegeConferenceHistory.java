package com.gym.roster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "college_conf_history", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_college_conf_history",
                columnNames = {"college_id", "start_year"})
})
public class CollegeConferenceHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false, foreignKey = @ForeignKey(name = "fk_college_conf_history_college"))
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
}
