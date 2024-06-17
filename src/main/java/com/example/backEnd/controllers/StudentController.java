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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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

    @GetMapping("/filter")
    public ResponseEntity<Collection<?>> searchByTermAndField(
            @RequestParam String field,
            @RequestParam String term) {

        Collection<?> results =
                studentService.findByFieldAndTerm(null, null, field, term, null);
        return ResponseEntity.ok(results);
    }
}
