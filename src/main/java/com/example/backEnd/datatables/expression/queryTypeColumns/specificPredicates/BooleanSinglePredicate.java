package com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.abs.IPredicate;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

public final class BooleanSinglePredicate implements IPredicate {

    @Override
    public Predicate create(String[] values, Expression<?> expression) {
        //if true plus false or nothing at all - we return all values without any predicate
        if (values.length != 1) return null;
        //true or false
        boolean booleanValue = Boolean.parseBoolean(values[0]);
        return Expressions.booleanTemplate("{0}", expression).eq(booleanValue);
    }
}
