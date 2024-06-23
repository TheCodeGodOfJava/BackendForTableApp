package com.example.backEnd.queryDsl.types;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.StringSinglePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StringSinglePredicateTest {

  @Test
  public void shouldCreatePredicateForMultiStringValue() {

    // given
    String[] values = {"firstValue"};

    var stringExpression = Expressions.stringTemplate("");

    // when
    Predicate predicate = new StringSinglePredicate().create(values, stringExpression);

    assertNotNull(predicate);
    assertEquals(" like %firstValue%", predicate.toString());
  }
}
