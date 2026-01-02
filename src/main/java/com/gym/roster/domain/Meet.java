package com.gym.roster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name =  "meet")   
public class Meet extends BaseEntity {

    @NotBlank(message = "Event date is required")
    @Column(name = "event_date", nullable = false)
    private Date eventDate;

    @NotBlank(message = "Event name is required")
    @Size(max = 200, message = "Name must have 200 or fewer characters")
    @Column(name = "event_name", nullable = false, length = 200)
    private String eventName;

    @Size(max = 100, message = "Location must have 100 or fewer characters")
    @Column(name = "location", length = 100)
    private String location;
}
