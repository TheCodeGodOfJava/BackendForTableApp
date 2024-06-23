package com.example.backEnd.queryDsl.types;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.NumberSinglePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NumberSinglePredicateTest {

  @Test
  public void shouldCreatePredicateForSingleNumberValue() {

    // given
    String[] values = {"1"};

    var numberExpression = Expressions.numberTemplate(Long.class, "");

    // when
    Predicate predicate = new NumberSinglePredicate().create(values, numberExpression);

    assertNotNull(predicate);
    assertEquals(" = 1", predicate.toString());
  }
}
