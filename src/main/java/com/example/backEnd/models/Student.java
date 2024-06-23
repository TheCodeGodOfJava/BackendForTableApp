package com.example.backEnd.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student extends AbstractEntity {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column()
    private Integer age;

    @Column()
    private Boolean gender = Boolean.TRUE;

    @Column(name = "enroll_date")
    private LocalDate enrollDate;

    @Column(columnDefinition = "text")
    private String about;
}
