package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.util.NumberUtils;

public final class NumberSinglePredicate implements IPredicate {

    @Override
    public Predicate create(String[] values, Expression<?> expression) {
        return Expressions.numberTemplate(Long.class, "{0}", expression)
                .eq(NumberUtils.parseNumber(values[0].trim(), Long.class));
    }
}
