package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringMultiPredicate implements IPredicate {

    @Override
    public Predicate create(String[] values, Expression<?> expression) {
        return buildPredicate(values, value -> likeExpression(value, expression));
    }

    private Predicate buildPredicate(String[] values, Function<? super String, ? extends BooleanExpression> function) {
        return ExpressionUtils.anyOf(Arrays.stream(values).map(function).collect(Collectors.toList()));
    }


    private BooleanExpression likeExpression(String value, Expression<?> expression) {
        return Expressions.stringTemplate("{0}", expression).like("%" + value + "%");
    }
}
