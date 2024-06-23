package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;

@FunctionalInterface
public interface IPredicate {
  Predicate create(String[] values, Expression<?> expression);
}
