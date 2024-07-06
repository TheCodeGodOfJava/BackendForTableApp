package com.example.backEnd.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
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

  @Column() private String country;

  @Column() private String state;

  @Column() private String city;

  @Column() private String phone;

  @Column() private String email;

  @Getter(AccessLevel.NONE)
  @ManyToMany
  @JoinTable(
      name = "student_professor",
      joinColumns = @JoinColumn(name = "student_id"),
      inverseJoinColumns = @JoinColumn(name = "professor_id"))
  private Set<Professor> professors = new HashSet<>();

  public void bindProfessor(Professor professor) {
    professors.add(professor);
    professor.addUniDirectionStudent(this);
  }

  public void unbindPermit(Professor professor) {
    professors.remove(professor);
    professor.removeUniDirectionStudent(this);
  }
}
