package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class LocalDateRangePredicate implements IPredicate {

  @Override
  public Predicate create(String[] value, Expression<?> expression) {
    String dateRangeObjectJson = value[0];
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    RangeDate rangeDate;
    try {
      rangeDate = objectMapper.readValue(dateRangeObjectJson, RangeDate.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    LocalDate start =
        rangeDate.getStart() != null ? rangeDate.getStart().toLocalDate() : LocalDate.ofEpochDay(0);
    LocalDate end = rangeDate.getEnd() != null ? rangeDate.getEnd().toLocalDate() : LocalDate.now();
    return Expressions.dateTimeTemplate(LocalDate.class, "{0}", expression).between(start, end);
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  private static class RangeDate {
    private LocalDateTime start;
    private LocalDateTime end;
  }
}
