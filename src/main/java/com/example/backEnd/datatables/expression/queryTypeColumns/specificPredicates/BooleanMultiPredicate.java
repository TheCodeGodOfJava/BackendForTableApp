package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public final class BooleanMultiPredicate implements IPredicate {

    @Override
    public Predicate create(String[] values, Expression<?> expression) {
        return Expressions.booleanTemplate("{0}", expression).in(true, false);
    }
}
