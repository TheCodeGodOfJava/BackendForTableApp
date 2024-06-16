package com.example.backEnd.controllers;

import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.models.projections.StudentProjection;
import com.example.backEnd.services.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/all")
    public ResponseEntity<DataTablesOutput<StudentProjection>> getAll(
            @Valid DataTablesInput input) {

        return new ResponseEntity<>(
                studentService.findAll(null, null, false, input), HttpStatus.OK);
    }
}
