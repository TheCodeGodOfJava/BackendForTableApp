package com.example.backEnd.models.projections;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorProjection {

  private String firstName;

  private String lastName;

  @Column() private String subject;

  @Column() private String phone;
}
