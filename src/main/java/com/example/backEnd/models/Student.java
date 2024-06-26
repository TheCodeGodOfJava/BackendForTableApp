package com.example.backEnd.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student extends AbstractEntity {
  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column() private Integer age;

  @Column() private Boolean gender = Boolean.TRUE;

  @Column(name = "enroll_date")
  private LocalDate enrollDate;

  @Column(columnDefinition = "text")
  private String about;

  @Column(name = "country")
  private String country;

  @Column(name = "state")
  private String state;

  @Column(name = "city")
  private String city;
}
