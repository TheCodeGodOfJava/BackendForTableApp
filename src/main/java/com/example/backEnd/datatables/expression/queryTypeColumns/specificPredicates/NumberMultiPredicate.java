package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.util.NumberUtils;

import java.util.Arrays;

public final class NumberMultiPredicate implements IPredicate {

    @Override
    public Predicate create(String[] values, Expression<?> expression) {

        Long[] longValues = Arrays.stream(values)
                .map(v -> NumberUtils.parseNumber(v, Long.class))
                .toArray(Long[]::new);

        return Expressions.numberTemplate(Long.class, "{0}", expression).in(longValues);
    }
}
