package com.example.backEnd.queryDsl.types;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.LocalDateTimeRangePredicate;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocalDateTimeRangePredicateTest {

    @Test
    public void shouldCreatePredicateForDateRange() {

        // given
        String[] values = {"2022-01-20T15:30:00#2022-02-20T15:30:00"};

        var dateTimeExpression = Expressions.dateTimeOperation(LocalDateTime.class, Ops.DateTimeOps.CURRENT_DATE);

        // when
        Predicate predicate = new LocalDateTimeRangePredicate().create(values, dateTimeExpression);

        assertNotNull(predicate);
        assertEquals("current_date() between 2022-01-20T00:00 and 2022-02-20T23:59", predicate.toString());
    }
}
