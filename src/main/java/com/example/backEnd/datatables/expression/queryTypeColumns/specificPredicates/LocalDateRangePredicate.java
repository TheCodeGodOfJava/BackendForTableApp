package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import java.time.LocalDate;

public final class LocalDateRangePredicate implements IPredicate {

    @Override
    public Predicate create(String[] value, Expression<?> expression) {
        String[] dateValues = value[0].split("#");
        String localDateStringStart = dateValues[0].split("T")[0];
        String localDateStringEnd = dateValues[1].split("T")[0];

        return Expressions.dateTimeTemplate(LocalDate.class, "{0}", expression)
                .between(LocalDate.parse(localDateStringStart), LocalDate.parse(localDateStringEnd));
    }
}
