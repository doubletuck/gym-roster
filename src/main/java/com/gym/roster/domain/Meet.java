package com.gym.roster.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Table(name =  "meet")    
public class Meet extends BaseEntity {

    @NotBlank(message = "Date is required")
    @Column(name = "event_date", nullable = false)
    private Date eventDate;

    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must have 200 or fewer characters")
    @Column(name = "name", nullable = false, length = 200)
    private String name;
}
