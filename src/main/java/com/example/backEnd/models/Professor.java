package com.example.backEnd.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "professors")
public class Professor extends AbstractEntity {
  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column()
  private String subject;

  @Column()
  private String phone;

  @Getter(AccessLevel.NONE)
  @ManyToMany(mappedBy = "professors")
  private Set<Student> students = new HashSet<>();

  public void addUniDirectionStudent(Student student) {
    this.students.add(student);
  }

  public void removeUniDirectionStudent(Student student) {
    this.students.remove(student);
  }
}
