package com.gym.roster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "coach", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_coach",
                columnNames = {"first_name", "last_name"})
})
public class Coach extends BaseEntity {

    @NotBlank(message = "First name is required")
    @Size(max = 30, message = "First name must have 30 or fewer characters")
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name must have 30 or fewer characters")
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;
}
