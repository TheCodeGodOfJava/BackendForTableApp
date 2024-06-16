package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import java.time.LocalTime;

public final class LocalTimeSinglePredicate implements IPredicate {

    @Override
    public Predicate create(String[] value, Expression<?> expression) {
        String[] dateValues = value[0].split("#");

        return Expressions.dateTimeTemplate(LocalTime.class, "{0}", expression)
                .between(LocalTime.parse(dateValues[0]), LocalTime.parse(dateValues[1]));
    }
}
