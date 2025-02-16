package com.example.backEnd.controllers;

import com.example.backEnd.datatables.mapping.DataTablesInput;
import com.example.backEnd.datatables.mapping.DataTablesOutput;
import com.example.backEnd.models.projections.ProfessorProjection;
import com.example.backEnd.services.ProfessorService;
import com.mysema.commons.lang.Pair;
import jakarta.validation.Valid;
import java.util.*;
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
        professorService.findByFieldAndTerm(
            masterId, masterType, field, term, dependencies, tableToggle, pageSize, currentPage);
    return ResponseEntity.ok(results);
  }

  @GetMapping("/getParentSelectValue")
  public ResponseEntity<List<?>> getParentSelectValue(
      @RequestParam String parentFieldAlias,
      @RequestParam String childFieldAlias,
      @RequestParam String childFieldValue) {

    List<?> result =
        professorService.getParentSelectValue(parentFieldAlias, childFieldAlias, childFieldValue);

    return ResponseEntity.ok(result);
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
