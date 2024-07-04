package com.example.backEnd.models.projections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFormProjection extends StudentProjection {

  private String phone;

  private String email;
}
