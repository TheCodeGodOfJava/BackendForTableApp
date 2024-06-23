package com.example.backEnd.models.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentProjection {
    private Long id;

    private String firstName;

    private String lastName;

    private Integer age;

    private Boolean gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate enrollDate;

    private String about;
}
