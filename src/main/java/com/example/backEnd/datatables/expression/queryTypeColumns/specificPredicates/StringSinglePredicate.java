package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public final class StringSinglePredicate implements IPredicate {

    @Override
    public Predicate create(String[] values, Expression<?> expression) {
        return Expressions.stringTemplate("{0}", expression).like("%" + values[0].trim() + "%");
    }
}
