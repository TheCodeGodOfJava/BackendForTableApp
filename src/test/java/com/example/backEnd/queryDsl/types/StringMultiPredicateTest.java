package com.example.backEnd.queryDsl.types;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.StringMultiPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StringMultiPredicateTest {

    @Test
    public void shouldCreatePredicateForMultiStringValue() {

        // given
        String[] values = {"firstValue", "secondValue", "thirdValue"};

        var stringExpression = Expressions.stringTemplate("");

        // when
        Predicate predicate = new StringMultiPredicate().create(values, stringExpression);

        assertNotNull(predicate);
        assertEquals(" like %firstValue% ||  like %secondValue% ||  like %thirdValue%", predicate.toString());
    }
}
