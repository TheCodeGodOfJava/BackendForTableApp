package com.example.backEnd.controllers;

import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.models.projections.StudentFormProjection;
import com.example.backEnd.models.projections.StudentProjection;
import com.example.backEnd.services.StudentService;
import com.mysema.commons.lang.Pair;
import jakarta.validation.Valid;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentService studentService;

  @GetMapping("/getOneById")
  public ResponseEntity<StudentFormProjection> getById(@RequestParam Long id) {
    var result = studentService.findByIdProjection(id);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/all")
  public ResponseEntity<DataTablesOutput<StudentProjection>> getAll(
      @RequestParam(required = false) Long masterId,
      @RequestParam(required = false) String masterType,
      @RequestParam(required = false, defaultValue = "false") Boolean tableToggle,
      @Valid DataTablesInput input) {

    return new ResponseEntity<>(
        studentService.findAll(masterId, masterType, tableToggle, input), HttpStatus.OK);
  }

  @GetMapping("/filter")
  public ResponseEntity<Pair<?, Collection<?>>> searchByTermAndField(
      @RequestParam String field,
      @RequestParam String term,
      @RequestParam(required = false) String[] depAliases,
      @RequestParam(required = false) String[] deps,
      @RequestParam(required = false) Long masterId,
      @RequestParam(required = false) String masterType,
      @RequestParam(required = false, defaultValue = "false") boolean tableToggle,
      @RequestParam(required = false) Long pageSize,
      @RequestParam(required = false) Long currentPage) {

    Map<String, String> dependencies = new HashMap<>();
    if (depAliases != null && deps != null) {
      for (int i = 0; i < depAliases.length; i++) {
        dependencies.put(depAliases[i], deps[i]);
      }
    }

    Pair<?, Collection<?>> results =
        studentService.findByFieldAndTerm(
            masterId, masterType, field, term, dependencies, tableToggle, pageSize, currentPage);
    return ResponseEntity.ok(results);
  }

  @GetMapping("/getParentSelectValue")
  public ResponseEntity<List<?>> getParentSelectValue(
      @RequestParam String parentFieldAlias,
      @RequestParam String childFieldAlias,
      @RequestParam String childFieldValue) {

    List<?> result =
        studentService.getParentSelectValue(parentFieldAlias, childFieldAlias, childFieldValue);

    return ResponseEntity.ok(result);
  }

  @PostMapping("/save")
  public ResponseEntity<StudentFormProjection> save(
      @RequestBody StudentFormProjection studentFormProjection) {
    var projection = studentService.save(studentFormProjection);
    var optional = Optional.ofNullable(studentFormProjection);
    var httpStatus =
        optional.map(detail -> HttpStatus.CREATED).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(projection, httpStatus);
  }

  @DeleteMapping("/remove")
  public ResponseEntity<Void> remove(@RequestBody List<Long> ids) {
    studentService.removeAll(ids);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/unbind")
  public ResponseEntity<Void> unbind(@RequestParam Long masterId, @RequestBody List<Long> ids) {
    studentService.unbindAll(masterId, ids);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/bind")
  public ResponseEntity<Void> bind(@RequestParam Long masterId, @RequestBody List<Long> ids) {
    studentService.bindAll(masterId, ids);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
