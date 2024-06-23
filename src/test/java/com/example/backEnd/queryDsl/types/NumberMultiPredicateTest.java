package com.example.backEnd.queryDsl.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.NumberMultiPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;

public class NumberMultiPredicateTest {

    @Test
    public void shouldCreatePredicateForMultiNumberValues() {

        // given
        String[] values = {"1", "2", "3"};

        var numberExpression = Expressions.numberTemplate(Long.class, "");

        // when
        Predicate predicate = new NumberMultiPredicate().create(values, numberExpression);

        assertNotNull(predicate);
        assertEquals(" in [1, 2, 3]", predicate.toString());
    }
}
