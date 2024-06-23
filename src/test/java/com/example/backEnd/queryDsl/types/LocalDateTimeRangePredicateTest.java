package com.example.backEnd.queryDsl.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.backEnd.datatables.expression.queryTypeColumns.specificPredicates.LocalDateRangePredicate;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LocalDateTimeRangePredicateTest {

    @Test
    public void shouldCreatePredicateForDateRange() {

        // given
        String[] values = {"{\"start\":\"2024-06-01T21:00:00.000Z\",\"end\":\"2024-06-19T21:00:00.000Z\"}"};


        var dateTimeExpression = Expressions.dateTimeOperation(LocalDateTime.class, Ops.DateTimeOps.CURRENT_DATE);

        // when
        Predicate predicate = new LocalDateRangePredicate().create(values, dateTimeExpression);

        assertNotNull(predicate);
        assertEquals("current_date() between 2024-06-01 and 2024-06-19", predicate.toString());
    }
}
