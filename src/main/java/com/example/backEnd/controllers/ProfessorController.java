package com.example.backEnd.controllers;

import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.models.projections.ProfessorProjection;
import com.example.backEnd.services.ProfessorService;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/professors")
public class ProfessorController {

  private final ProfessorService professorService;

  @GetMapping("/getOneById")
  public ResponseEntity<ProfessorProjection> getById(@RequestParam Long id) {
    var result = professorService.findByIdProjection(id);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/all")
  public ResponseEntity<DataTablesOutput<ProfessorProjection>> getAll(
      @RequestParam(required = false) Long masterId,
      @RequestParam(required = false) String masterType,
      @RequestParam(required = false, defaultValue = "false") Boolean tableToggle,
      @Valid DataTablesInput input) {

    return new ResponseEntity<>(
        professorService.findAll(masterId, masterType, tableToggle, input), HttpStatus.OK);
  }

  @GetMapping("/filter")
  public ResponseEntity<Collection<?>> searchByTermAndField(
      @RequestParam String field,
      @RequestParam String term,
      @RequestParam(required = false) String depAlias,
      @RequestParam(required = false) String dep) {

    Collection<?> results =
        professorService.findByFieldAndTerm(null, null, field, term, depAlias, dep);
    return ResponseEntity.ok(results);
  }

  @PostMapping("/save")
  public ResponseEntity<ProfessorProjection> save(
      @RequestBody ProfessorProjection ProfessorProjection) {
    var projection = professorService.save(ProfessorProjection);
    var optional = Optional.ofNullable(ProfessorProjection);
    var httpStatus =
        optional.map(detail -> HttpStatus.CREATED).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(projection, httpStatus);
  }

  @DeleteMapping("/remove")
  public ResponseEntity<Void> remove(@RequestBody List<Long> ids) {
    professorService.removeAll(ids);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/unbind")
  public ResponseEntity<Void> unbind(@RequestParam Long masterId, @RequestBody List<Long> ids) {
    professorService.unbindAll(masterId, ids);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/bind")
  public ResponseEntity<Void> bind(@RequestParam Long masterId, @RequestBody List<Long> ids) {
    professorService.bindAll(masterId, ids);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
