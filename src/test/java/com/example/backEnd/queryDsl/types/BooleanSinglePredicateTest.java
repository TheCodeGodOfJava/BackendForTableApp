package com.example.backEnd.queryDsl.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.BooleanSinglePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;

public class BooleanSinglePredicateTest {

    @Test
    public void shouldCreatePredicateForSingleBoolean() {

        // given
        String[] values = {"true"};

        var booleanExpression = Expressions.booleanTemplate("");

        // when
        Predicate predicate = new BooleanSinglePredicate().create(values, booleanExpression);

        assertNotNull(predicate);
        assertEquals(" = true", predicate.toString());
    }
}
